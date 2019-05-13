using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace TilesLayout
{
    public class TilesLayoutContent
    {
        public TilesLayoutCell[,] Items;

        public float Width { get; private set; }
        public float Height { get; private set; }

        public TilesLayoutContent(int rows, int columns)
        {
            Items = new TilesLayoutCell[columns, rows];
        }

        public int Rows
        {
            get
            {
                return Items.GetLength(1);
            }
        }

        public int Columns
        {
            get
            {
                return Items.GetLength(0);
            }
        }

        public void SetItems(TilesLayoutCell[,] items)
        {
            if (items.GetLength(0) != Columns || items.GetLength(1) != Rows)
            {
                throw new InvalidOperationException("Passed items size is different from the content's size.");
            }

            Items = items;
            Measure();
        }

        public void SetCell(TilesLayoutCell cell, int x, int y)
        {
            Items[x, y] = cell;
        }

        public void Measure()
        {
            Width = MeasueWidth();
            Height = MeasureHeight();
        }

        private float MeasueWidth()
        {
            float measuredWidth = 0;
            for (int y = 0; y < Rows; y++)
            {
                float width = 0;
                for (int x = 0; x < Columns; x++)
                {
                    if (Items[x, y] == null)
                    {
                        width += 1;
                    }
                    else
                    {
                        width += Items[x, y].MeasuredWidth;
                    }
                }

                if (measuredWidth < width)
                {
                    measuredWidth = width;
                }
            }

            return measuredWidth;
        }

        private float MeasureHeight()
        {
            float measuredHeight = 0;
            for (int x = 0; x < Columns; x++)
            {
                float height = 0;
                for (int y = 0; y < Rows; y++)
                {
                    if (Items[x, y] == null)
                    {
                        height += 1;
                    }
                    else
                    {
                        height += Items[x, y].MeasuredHeight;
                    }
                }

                if (measuredHeight < height)
                {
                    measuredHeight = height;
                }
            }

            return measuredHeight;
        }
    }
}