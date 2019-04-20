using LetterTile;
using Level.Repository;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using Util;

namespace Level
{
    public class LevelLoader : MonoBehaviour
    {
        private LevelDataRepository LevelDataRepository;

        public GameObject LetterGameObject;
        public GameObject ObstacleGameObject;
        public GameObject EmptyGameObject;

        public int TilesPerRow;
        public int TilesPerColumn;

        // Start is called before the first frame update
        void Start()
        {
            LevelDataRepository = GameObject.Find("LevelDataRepository").GetComponent("LevelDataRepository") as LevelDataRepository;

            var content = new TilesLayout.TilesLayoutContent(TilesPerColumn, TilesPerRow);

            CreateCells(content);

            var tilesLayout = GetTilesLayout();

            InitCellLetters(tilesLayout, content);
            AddSideCollisions(content);
        }

        private void CreateCells(TilesLayout.TilesLayoutContent content)
        {
            for (int row = 0; row < TilesPerRow; row++) 
            {
                for (int col = 0; col < TilesPerColumn; col++)
                {
                    var cell = new TilesLayout.TilesLayoutCell();
                    content.SetCell(cell, row, col);
                }
            }
        }

        private TilesLayout.TilesLayout GetTilesLayout()
        {
            var tilesLayoutObject = GameObject.Find("TilesLayout");
            return tilesLayoutObject.GetComponent(typeof(TilesLayout.TilesLayout)) as TilesLayout.TilesLayout;
        }

        private void AddSideCollisions(TilesLayout.TilesLayoutContent content)
        {
            // add extra height above the layout for the starting vertical position of the character
            // and add the botom when it's supposed to fall off
            float extraHeight = content.Items[0, 0].MeasuredHeight;

            float edgeHalfWidth = content.Width * 0.5f;
            float edgeHalfHeight = content.Height * 0.5f + extraHeight;

            var colliders = GetComponents<EdgeCollider2D>();

            colliders[0].points = new List<Vector2>
            {
                new Vector2(-edgeHalfWidth, edgeHalfHeight),
                new Vector2(-edgeHalfWidth, -edgeHalfHeight)
            }.ToArray();

            colliders[1].points = new List<Vector2>
            {
                new Vector2(edgeHalfWidth, -edgeHalfHeight),
                new Vector2(edgeHalfWidth, edgeHalfHeight)
            }.ToArray();
        }

        private void InitCellLetters(TilesLayout.TilesLayout tilesLayout, TilesLayout.TilesLayoutContent content)
        {
            var obstaclePositioner = new ObstaclePositioner();
            var emptyCellPositioner = new EmptyCellPositioner();

            var cells = new PathCell[content.Columns, content.Rows];

            cells.ForeachIndexed((x, y, cell) => cells[x, y] = new PathCell(x, y));

            int pathLength = EnsurePathExists(cells);
            var letters = GenerateLetters(pathLength);
            int maxObstacles = cells.Length - (letters.Count >= pathLength ? (letters.Count) : (pathLength));

            PlaceRemainingLetters(cells, pathLength, letters);
            obstaclePositioner.PlaceObstacles(cells, maxObstacles);
            emptyCellPositioner.PlaceEmptyCells(cells);

            cells.Print();
            RenderContent(content, cells, letters);

            tilesLayout.Content = content;

            var letterGenerator = LetterGenerator(letters).GetEnumerator();
            letterGenerator.MoveNext();

            cells.Flatten()
                .Where(cell => cell.CellType == PathCell.Type.LETTER)
                .ToList()
                .ForEach(cell =>
                {
                    var gameObject = content.Items[cell.Position.x, cell.Position.y].GameObject;
                    var binding = gameObject.GetComponent(typeof(LetterTileBinding)) as LetterTileBinding;
                    binding.Letter = letterGenerator.Current;
                    letterGenerator.MoveNext();
                });
        }

        private static void PlaceRemainingLetters(PathCell[,] cells, int pathLength, List<string> letters)
        {
            cells.Flatten()
                            .Where(cell => cell.CellType != PathCell.Type.LETTER)
                            .Take(letters.Count - pathLength)
                            .ToList()
                            .ShuffleWith(new System.Random())
                            .ForEach(cell => cell.CellType = PathCell.Type.LETTER);
        }

        private void RenderContent(TilesLayout.TilesLayoutContent content, PathCell[,] cells, List<string> letters)
        {
            cells.ForeachIndexed((x, y, cell) =>
            {
                GameObject tileGameObject = null;
                switch (cell.CellType)
                {
                    case PathCell.Type.LETTER:
                        tileGameObject = LetterGameObject;
                        break;
                    case PathCell.Type.OBSTACLE:
                        tileGameObject = ObstacleGameObject;
                        break;
                    case PathCell.Type.EMPTY:
                        tileGameObject = EmptyGameObject;
                        break;
                }

                content.Items[x, y].GameObject = tileGameObject;
            });
        }

        private List<string> GenerateLetters(int pathLength)
        {
            var letterContentCreator = new LetterContentCreator();
            var parameters = new LetterContentCreator.Params(LevelDataRepository.GetLevelData().HintWords)
            {
                MinLength = pathLength,
                MaxLength = TilesPerRow * TilesPerColumn
            };

            var letters = letterContentCreator.GenerateLettersFromWords(parameters);
            return letters;
        }

        private int EnsurePathExists(PathCell[,] cells)
        {
            var random = new System.Random();
            var pathCreator = new PathCreator();
            var pathLength = pathCreator.MarkPath(cells, random.Next(TilesPerRow));
            return pathLength;
        }

        private IEnumerable<string> LetterGenerator(List<string> letters)
        {
            foreach (string letter in letters)
            {
                yield return letter;
            }
        }
    }

    public static class TempExt
    {
        public static void Print(this PathCell[,] cells)
        {
            var stringBuilder = new StringBuilder();
            for (int y = 0; y < cells.GetLength(1); y++)
            {
                var l = new List<PathCell>();
                for (int x = 0; x < cells.GetLength(0); x++)
                {
                    l.Add(cells[x, y]);
                }
                stringBuilder.AppendLine(string.Join(", ", l));
            }

            Debug.Log(stringBuilder.ToString());
        }
    }
}