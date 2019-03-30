using LetterTile;
using Level.Repository;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level
{
    public class LevelLoader : MonoBehaviour
    {
        private LevelDataRepository LevelDataRepository; 

        public GameObject TileGameObject;
        public GameObject ObstacleGameObject;

        public int TilesPerRow;
        public int TilesPerColumn;

        // Start is called before the first frame update
        void Start()
        {
            LevelDataRepository = GameObject.Find("LevelDataRepository").GetComponent("LevelDataRepository") as LevelDataRepository;

            var content = new TilesLayout.TilesLayoutContent(TilesPerColumn, TilesPerRow);

            CreateCells(content);

            var tilesLayout = GetTilesLayout();

            tilesLayout.Content = content;

            InitCellLetters(content);
        }

        private void CreateCells(TilesLayout.TilesLayoutContent content)
        {
            for (int col = 0; col < TilesPerColumn; col++)
            {
                for (int row = 0; row < TilesPerRow; row++)
                {
                    var cell = new TilesLayout.TilesLayoutCell(TileGameObject);
                    content.SetCell(cell, row, col);
                }
            }
        }

        private TilesLayout.TilesLayout GetTilesLayout()
        {
            var tilesLayoutObject = GameObject.Find("TilesLayout");
            return tilesLayoutObject.GetComponent(typeof(TilesLayout.TilesLayout)) as TilesLayout.TilesLayout;
        }

        private void InitCellLetters(TilesLayout.TilesLayoutContent content)
        {
            var letterContentCreator = new LetterContentCreator();
            var parameters = new LetterContentCreator.Params(LevelDataRepository.GetLevelData().HintWords)
            {
                MinLength = TilesPerRow * TilesPerColumn,
                MaxLength = TilesPerRow * TilesPerColumn
            };

            var letters = letterContentCreator.GenerateLettersFromWords(parameters);
            int i = 0;
            foreach (TilesLayout.TilesLayoutCell cell in content.Items)
            {
                var binding = cell.GameObject.GetComponent(typeof(LetterTileBinding)) as LetterTileBinding;
                binding.Letter = letters[i];
                i++;
            }
        }
    }

}