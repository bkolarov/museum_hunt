using System.Collections.Generic;
using UnityEngine;
using System.Linq;
using Util;

namespace Level
{
    public class ObstaclePositioner
    {
        private readonly System.Random Random = new System.Random();

        public void PlaceObstacles(PathCell[,] cells, int maxObstacles)
        {
            cells.Flatten()
                .Where(cell => !cell.Visited)
                .Select(cell => cell)
                .ToList()
                .ShuffleWith(Random)
                .Take(maxObstacles)
                .ToList()
                .ForEach(cell =>
                {
                    
                    if (CanPlaceObstacle(cells, cell))
                    {
                        cell.Visited = true;
                        cell.CellType = PathCell.Type.OBSTACLE;
                    }
                });
        }

        private bool CanPlaceObstacle(PathCell[,] inCells, PathCell atCell)
        {
            foreach (Vector2Int location in atCell.GetNeighboursLocations())
            {
                // Make sure we don't create a dead end for the player.
                if (inCells.Contains(location) && inCells[location.x, location.y].CellType != PathCell.Type.OBSTACLE)
                {
                    return true;
                }
            }

            return false;
        }
    }
}