using System.Collections.Generic;
using UnityEngine;

namespace Level
{
    public class PathCell
    {
        public Vector2Int Position { get; set; }

        public bool Visited { get; set; } = false;

        public Type CellType { get; set; } = Type.UNKNOWN;

        public PathCell(int x, int y)
        {
            Position = new Vector2Int(x, y);
        }

        public List<Vector2Int> GetNeighboursLocations()
        {
            return new List<Vector2Int>
            {
                LeftNeighbour,
                TopNeighbour,
                RightNeighbour,
                BottomNeighour,
                TopLeftNeighbour,
                TopRightNeighbour,
                BottomRightNeighbour,
                BottomLeftNeighbour
            };
        }

        public int X
        {
            get
            {
                return Position.x;
            }
            set
            {
                Position.Set(value, Y);
            }
        }

        public int Y
        {
            get
            {
                return Position.y;
            }
            set
            {
                Position.Set(X, value);
            }
        }

        public Vector2Int LeftNeighbour
        {
            get
            {
                return new Vector2Int(X - 1, Y);
            }
        }

        public Vector2Int TopNeighbour
        {
            get
            {
                return new Vector2Int(X, Y - 1);
            }
        }

        public Vector2Int RightNeighbour
        {
            get
            {
                return new Vector2Int(X + 1, Y);
            }
        }

        public Vector2Int BottomNeighour
        {
            get
            {
                return new Vector2Int(X, Y + 1);
            }
        }

        public Vector2Int TopLeftNeighbour
        {
            get
            {
                return TopNeighbour + LeftNeighbour;
            }
        }

        public Vector2Int TopRightNeighbour
        {
            get
            {
                return TopNeighbour + RightNeighbour;
            }
        }

        public Vector2Int BottomRightNeighbour
        {
            get
            {
                return BottomNeighour + RightNeighbour;
            }
        }

        public Vector2Int BottomLeftNeighbour
        {
            get
            {
                return BottomNeighour + LeftNeighbour;
            }
        }

        public enum Type
        {
            LETTER, OBSTACLE, EMPTY, UNKNOWN
        }

        public override string ToString()
        {
            return CellType.ToString();
        }
    }
}