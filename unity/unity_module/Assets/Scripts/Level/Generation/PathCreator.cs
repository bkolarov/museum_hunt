using UnityEngine;

namespace Level
{
    public class PathCreator
    {
        private System.Random Rnd = new System.Random();

        private readonly PathDirection[] PossibleDirections = new PathDirection[] { PathDirection.LEFT, PathDirection.RIGHT, PathDirection.DOWN };

        public int MarkPath(PathCell[,] cells, int x = 0, int y = 0, int pathLength = 0)
        {
            var width = cells.GetLength(0);
            var height = cells.GetLength(1);

            cells[x, y].Visited = true;
            cells[x, y].CellType = PathCell.Type.LETTER;
            pathLength++;

            if (y == height - 1)
            {
                return pathLength;
            }

            Vector2Int? neighbour;
            do
            {
                neighbour = cells[x, y].GetNeighbour(PossibleDirections[Rnd.Next(0, 3)], cells);
            } while (neighbour == null || cells[neighbour.Value.x, neighbour.Value.y].Visited);
            
            return MarkPath(cells, neighbour.Value.x, neighbour.Value.y, pathLength);
        }
    }

    public enum PathDirection
    {
        LEFT, UP, RIGHT, DOWN
    }
}