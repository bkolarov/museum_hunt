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
        public GameObject FinishLine;

        public int TilesPerRow;
        public int TilesPerColumn;

        // Start is called before the first frame update
        void Start()
        {
            LevelDataRepository = GameObject.Find("LevelDataRepository").GetComponent("LevelDataRepository") as LevelDataRepository;

            var content = new TilesLayout.TilesLayoutContent(TilesPerColumn, TilesPerRow);

            CreateCells(content);

            var tilesLayout = GetTilesLayout();

            GenerateLevel(tilesLayout, content);
            AddSideCollisions(content);
            AddFinishLine();
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

            colliders[0].points = new Vector2[]
            {
                new Vector2(-edgeHalfWidth, edgeHalfHeight),
                new Vector2(-edgeHalfWidth, -edgeHalfHeight)
            };

            colliders[1].points = new Vector2[]
            {
                new Vector2(edgeHalfWidth, -edgeHalfHeight),
                new Vector2(edgeHalfWidth, edgeHalfHeight)
            };
        }

        private void AddFinishLine()
        {
            var cameraHeight = Camera.main.orthographicSize * 2;
            var screenAspect = (float)Screen.width / Screen.height;
            var camWidth = screenAspect * 2f * Camera.main.orthographicSize;
            var finishLineGameObj = Instantiate(FinishLine);
            var finishLineCollider = finishLineGameObj.GetComponent<EdgeCollider2D>();

            finishLineCollider.transform.position = new Vector3(0, -cameraHeight / 2);
            finishLineCollider.points = new Vector2[]
            {
                new Vector2(-camWidth / 2, 0),
                new Vector2(camWidth / 2, 0)
            };
        }

        private void GenerateLevel(TilesLayout.TilesLayout tilesLayout, TilesLayout.TilesLayoutContent content)
        {
            var obstaclePositioner = new ObstaclePositioner();
            var emptyCellPositioner = new EmptyCellPositioner();

            var cells = new PathCell[content.Columns, content.Rows];

            cells.ForeachIndexed((x, y, cell) => cells[x, y] = new PathCell(x, y));

            int pathLength = EnsurePathExists(cells);
            var letters = GenerateLetters(pathLength);
            int maxObstacles = cells.Length - (letters.Count >= pathLength ? (letters.Count) : (pathLength));

            obstaclePositioner.PlaceObstacles(cells, maxObstacles);
            PlaceRemainingLetters(cells, pathLength, letters);
            emptyCellPositioner.PlaceEmptyCells(cells);

            cells.Print();

            GenerateGameObjects(cells, tilesLayout, content, letters);
        }

        private void GenerateGameObjects(PathCell[,] cells, TilesLayout.TilesLayout tilesLayout, TilesLayout.TilesLayoutContent content, List<string> letters)
        {
            RenderContent(tilesLayout, content, cells);

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
                            .Where(cell => cell.CellType != PathCell.Type.OBSTACLE)
                            .Take(letters.Count - pathLength)
                            .ToList()
                            .ShuffleWith(new System.Random())
                            .ForEach(cell => cell.CellType = PathCell.Type.LETTER);
        }

        private void RenderContent(TilesLayout.TilesLayout tilesLayout, TilesLayout.TilesLayoutContent content, PathCell[,] cells)
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

            tilesLayout.Content = content;
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
}