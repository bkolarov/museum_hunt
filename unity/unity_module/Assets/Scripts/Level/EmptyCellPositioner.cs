using System.Linq;
using Util;

namespace Level
{
    public class EmptyCellPositioner
    {
        public void PlaceEmptyCells(PathCell[,] cells)
        {
            cells.Flatten()
                .Where(cell => cell.CellType == PathCell.Type.UNKNOWN)
                .Select(cell => cell)
                .ToList()
                .ForEach(cell => cell.CellType = PathCell.Type.EMPTY);
        }
    }
}