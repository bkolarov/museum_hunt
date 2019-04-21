using LetterTile;
using Level.Repository;
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

    // Start is called before the first frame update
    void Start()
    {
        var tilesLayout = GameObject.Find("TilesLayout").GetComponent<TilesLayout.TilesLayout>();
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

    // Update is called once per frame
    void Update()
    {

    }

    public void OnBackClick()
    {
        
    }

    public void OnHelpClick()
    {
        
    }

    private void OnTileClick(LetterTileBinding binding)
    {
        TextPanelBinding.Text += binding.Letter;
    }
}
