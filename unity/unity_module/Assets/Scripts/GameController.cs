using LetterTile;
using Level.Repository;
using PoqXert.MessageBox;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using UI;
using UnityEngine;
using Util;

public class GameController : MonoBehaviour
{

    public GameObject LevelRepositoryObject;
    public GameObject BackButton;
    public GameObject HelpButton;
    public GameObject TextPanel;

    private TextPanelBinding TextPanelBinding;

    private LevelDataRepository LevelDataRepository;

    //private List<string> LetterPlaceHolders;
    private ObservableCollection<string> SelectedLettersObservable = new ObservableCollection<string>();
    private Stack<LetterTileBinding> SelectedTilesStack = new Stack<LetterTileBinding>();

    private ISet<string> WordsSet;
    private int LongestWordLength;

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
            id = 1,
            message = "Нивото няма да бъде запазено.\nСигурен/а ли сте, че искате да излезете.",
            negativeCallback = id =>
            {
                Debug.Log("Abort exit!");
                MsgBox.Close();
            },
            negativeButtonText = "Не",
            positiveCallback = id =>
            {
                Debug.Log("Proceed with exit!");
                MsgBox.Close();
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
        new MsgBox.Builder
        {
            id = 1,
            message = "Минете нивото, за да отключите загадка.",
            positiveCallback = id =>
            {
                Debug.Log("Close info!");
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
