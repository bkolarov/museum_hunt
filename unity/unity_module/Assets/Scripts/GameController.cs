using LetterTile;
using Level;
using Level.Repository;
using PlatformApplication;
using PoqXert.MessageBox;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using UI;
using UnityEngine;
using UnityEngine.Events;
using Util;

public class GameController : MonoBehaviour
{
    private static readonly int ID_QUIT_CONFIRMATION_DIALOG = 0;
    private static readonly int ID_HELPDIALOG = 1;

    public GameObject LevelRepositoryObject;
    public GameObject BackButton;
    public GameObject HelpButton;
    public GameObject TextPanel;
    public GameObject Player;

    private TextPanelBinding TextPanelBinding;

    private LevelDataRepository LevelDataRepository;

    //private List<string> LetterPlaceHolders;
    private ObservableCollection<string> SelectedLettersObservable = new ObservableCollection<string>();
    private Stack<LetterTileBinding> SelectedTilesStack = new Stack<LetterTileBinding>();

    private ISet<string> WordsSet;
    private int LongestWordLength;
    private ISet<string> FoundWords = new HashSet<string>();

    // Start is called before the first frame update
    void Start()
    {
        var tilesLayout = GameObject.Find("TilesLayout").GetComponent<TilesLayout.TilesLayout>();

        SelectedLettersObservable.CollectionChanged += (sender, args) =>
        {
            TextPanelBinding.Text = string.Join("", SelectedLettersObservable);
        };

        tilesLayout.OnGameObjectClick += (gameObject) =>
        {
            var tileBinding = gameObject.GetComponent<LetterTileBinding>();
            if (tileBinding != null)
            {
                OnTileClick(tileBinding);
            }
        };

        LevelDataRepository = LevelRepositoryObject.GetComponent<LevelDataRepository>();
        TextPanelBinding = TextPanel.GetComponent<TextPanelBinding>();

        var hintWords = LevelDataRepository.GetLevelData().HintWords;
        WordsSet = hintWords.Select(word => word.ToUpper()).ToSet();
        LongestWordLength = (from word in hintWords
                             orderby word.Length
                             select word.Length).Last();

        GameObject.FindGameObjectWithTag("Finish")
            .GetComponent<Level.FinishLineWatcher>()
            .OnPass.AddListener(OnGameFinish);

        ShowHelpDialog();
    }

    void Update()
    {
        if (Input.GetKeyUp(KeyCode.Escape))
        {
            OnBackPressed();
        }
    }

    protected void OnBackPressed()
    {
        if (MsgBox.isOpen)
        {
            MsgBox.Close();
        } else
        {
            OnBackClick();
        }
    }

    private IEnumerable<string> CreateEmpty(int size)
    {
        for (int i = 0; i < size; i++)
        {
            yield return null;
        }
    }

    public void OnBackClick()
    {
        new MsgBox.Builder
        {
            id = ID_QUIT_CONFIRMATION_DIALOG,
            message = "Нивото няма да бъде запазено.\nСигурен/а ли сте, че искате да излезете.",
            negativeCallback = id =>
            {
                MsgBox.Close();
            },
            negativeButtonText = "Не",
            positiveCallback = id =>
            {
                MobileApplication.Quit();
            },
            positiveButtonText = "Да",
            style = MsgBoxStyle.Information,
            buttons = MsgBoxButtons.YES_NO,
            modal = true
        }
       .Show();
    }

    public void OnHelpClick()
    {
        ShowHelpDialog();
    }

    private void ShowHelpDialog()
    {
        new MsgBox.Builder
        {
            id = ID_HELPDIALOG,
            message = "Минете нивото, за да отключите загадка.",
            positiveCallback = id =>
            {
                MsgBox.Close();
            },
            style = MsgBoxStyle.Information,
            buttons = MsgBoxButtons.OK,
            modal = true,
            positiveButtonText = "Добре"
        }
        .Show();
    }

    public void OnClearClick()
    {
        ClearSelection();
    }

    public void OnGameFinish()
    {
        Destroy(Player);
        MobileApplication.Quit(new ResultData(FoundWords.ToArray()));
    }

    private void OnTileClick(LetterTileBinding binding)
    {
        if ((binding.TileState == LetterTileBinding.State.SELECTED || binding.TileState == LetterTileBinding.State.ERROR)
            && SelectedTilesStack.Peek() != binding) return;

        var currentWord = string.Join("", SelectedLettersObservable);
        string newWord;

        if (binding.TileState == LetterTileBinding.State.IDLE)
        {
            SelectedTilesStack.Push(binding);
            SelectedLettersObservable.Add(binding.Letter);
            newWord = currentWord + binding.Letter;
        }
        else
        {
            var popped = SelectedTilesStack.Pop();
            popped.TileState = LetterTileBinding.State.IDLE;
            SelectedLettersObservable.RemoveAt(SelectedLettersObservable.Count - 1);
            newWord = currentWord.Substring(0, currentWord.Length - 1);
        }

        switch (CheckWord(newWord))
        {
            case WordCheckResult.CONTAINS:
                SelectedTilesStack.Apply(tile => tile.TileState = LetterTileBinding.State.SELECTED);
                break;
            case WordCheckResult.MATCH:
                SelectedTilesStack.Apply(tile => tile.TileState = LetterTileBinding.State.INACTIVE);
                WordsSet.Remove(newWord);
                ClearSelection();
                FoundWords.Add(newWord);
                break;
            case WordCheckResult.ERROR:
                SelectedTilesStack.Apply(tile => tile.TileState = LetterTileBinding.State.ERROR);
                break;
        }
    }

    private void ClearSelection()
    {
        SelectedLettersObservable.Clear();
        SelectedTilesStack.Apply(binding =>
        {
            if (binding.TileState != LetterTileBinding.State.INACTIVE)
            {
                binding.TileState = LetterTileBinding.State.IDLE;
            }
        });
        SelectedTilesStack.Clear();
    }

    private LetterTileBinding.State GetStateForResult(WordCheckResult result)
    {
        LetterTileBinding.State state;
        switch (result)
        {
            case WordCheckResult.CONTAINS:
            case WordCheckResult.MATCH:
                state = LetterTileBinding.State.SELECTED;
                break;
            case WordCheckResult.ERROR:
                state = LetterTileBinding.State.ERROR;
                break;
            default:
                throw new InvalidOperationException($"Could not handle unknown result: {result}");
        }

        return state;
    }

    private WordCheckResult CheckWord(string queryWord)
    {
        if (WordsSet.Contains(queryWord))
        {
            return WordCheckResult.MATCH;
        }

        if (queryWord.Length > LongestWordLength)
        {
            return WordCheckResult.ERROR;
        }

        if (WordsSet.Any(word => word.StartsWith(queryWord)))
        {
            return WordCheckResult.CONTAINS;
        }

        return WordCheckResult.ERROR;
    }

    private enum WordCheckResult
    {
        CONTAINS, MATCH, ERROR
    }
}
