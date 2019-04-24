using LetterTile;
using Level.Repository;
using PoqXert.MessageBox;
using System;
using System.Collections;
using System.Collections.Generic;
using UI;
using UnityEngine;

public class GameController : MonoBehaviour
{

    public GameObject LevelRepositoryObject;
    public GameObject BackButton;
    public GameObject HelpButton;
    public GameObject TextPanel;

    private TextPanelBinding TextPanelBinding;

    private LevelDataRepository LevelDataRepository;

    private List<string> LetterPlaceHolders;
    private Stack<LetterTileBinding> SelectedTilesStack = new Stack<LetterTileBinding>();

    // Start is called before the first frame update
    void Start()
    {
        var tilesLayout = GameObject.Find("TilesLayout").GetComponent<TilesLayout.TilesLayout>();
        LetterPlaceHolders = new List<string>();



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
            message = "Ала бала портокала?",
            negativeCallback = id =>
            {
                Debug.Log("Abort exit!");
                MsgBox.Close();
            },
            positiveCallback = id =>
            {
                Debug.Log("Proceed with exit!");
                MsgBox.Close();
            },
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
            message = "Ала бала портокала?",
            positiveCallback = id =>
            {
                Debug.Log("Close info!");
                MsgBox.Close();
            },
            style = MsgBoxStyle.Information,
            buttons = MsgBoxButtons.OK,
            modal = true
        }
        .Show();
    }

    private void OnTileClick(LetterTileBinding binding)
    {
        if (binding.Selected && SelectedTilesStack.Peek() != binding) return;

        binding.Selected = !binding.Selected;

        if (binding.Selected)
        {
            SelectedTilesStack.Push(binding);
            LetterPlaceHolders.Add(binding.Letter);
        }
        else
        {
            SelectedTilesStack.Pop();
            LetterPlaceHolders.RemoveAt(LetterPlaceHolders.Count - 1);
        }
        TextPanelBinding.Text = string.Join("", LetterPlaceHolders);
    }
}
