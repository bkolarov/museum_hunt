using UnityEngine;

namespace Level
{
    public static class TileGenerationExtensions
    {
        public static Vector2Int? GetNeighbour(this PathCell cell, PathDirection direction, PathCell[,] cells)
        {
            Vector2Int neighbour;
            switch (direction)
            {
                case PathDirection.LEFT:
                    neighbour = cell.LeftNeighbour;
                    break;
                case PathDirection.UP:
                    neighbour = cell.TopNeighbour;
                    break;
                case PathDirection.RIGHT:
                    neighbour = cell.RightNeighbour;
                    break;
                case PathDirection.DOWN:
                    neighbour = cell.BottomNeighour;
                    break;
                default:
                    return null;
            }

            var isInBounds = cells.Contains(neighbour);
            var visited = isInBounds ? cells[neighbour.x, neighbour.y].Visited : false;

            if (isInBounds && !visited)
            {
                return neighbour;
            }
            else
            {
                return null;
            }
        }

        public static bool Contains(this PathCell[,] cells, Vector2Int position)
        {
            RectInt rectInt = new RectInt(0, 0, cells.GetLength(0), cells.GetLength(1));
            return rectInt.Contains(position);
        }
    }

}