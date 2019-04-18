using LetterTile;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace TilesLayout
{
    public class TilesLayout : MonoBehaviour, IClickable
    {
        public int Columns = 0;
        public int Rows = 0;

        public float PaddingLeft = 0;
        public float PaddingRight = 0;

        private TilesLayoutContent _Content;
        private GameObjectClickListener OnItemClick;

        public event GameObjectClickListener OnGameObjectClick;

        public TilesLayoutContent Content
        {
            get
            {
                return _Content;
            }
            set
            {
                AssertGridSize(value);
                InvalidateContent();
                _Content = value;
                DisplayContent(value);
            }
        }

        // Start is called before the first frame update
        void Start()
        {
            OnItemClick = (gameObject) => OnGameObjectClick?.Invoke(gameObject);
        }

        private void DisplayContent(TilesLayoutContent content)
        {
            content.Measure();

            var fullWidth = content.Width + PaddingLeft + PaddingRight;
            AdjustCamera(fullWidth);

            for (int column = 0; column < Columns; column++)
            {
                float startX;
                if (column == 0)
                {
                    startX = -fullWidth / 2 + PaddingLeft;
                }
                else
                {
                    var previousCell = content.Items[column - 1, 0];
                    startX = previousCell.GameObject.transform.localPosition.x + previousCell.MeasuredWidth / 2;
                }

                // TODO - Think of a better way to adjust the starting Y position.
                var startY = -content.Height / 2f - 1;
                for (int row = 0; row < Rows; row++)
                {
                    var item = content.Items[column, row];
                    var x = startX + item.MeasuredWidth / 2;
                    var y = startY + item.MeasuredHeight / 2;

                    if (item.GameObject != null)
                    {
                        item.GameObject = Instantiate(item.GameObject, new Vector3(x, y, 0), item.GameObject.transform.rotation);
                        if (item.GameObject.GetComponent(typeof(IClickableProvider)) is IClickableProvider iClickableProvider)
                        {
                            iClickableProvider.GetIClickable().OnGameObjectClick += OnItemClick;
                        }
                    }

                    startY += item.MeasuredHeight;
                }
            }
        }

        private void InvalidateContent()
        {
            if (Content != null && Content.Items != null)
            {
                foreach (TilesLayoutCell cell in Content.Items)
                {
                    Destroy(cell.GameObject);
                }

                Content = null;
            }
        }

        private void AdjustCamera(float width)
        {
            var screenAspect = (float)Screen.width / Screen.height;
            var camWidth = screenAspect * 2f * Camera.main.orthographicSize;

            Camera.main.orthographicSize = width / (screenAspect * 2f);
        }

        private void AssertGridSize(TilesLayoutContent content)
        {
            if (content.Columns != Columns || content.Rows != Rows)
            {
                throw new System.InvalidOperationException($"Attempt to set content with {content.Rows} rows and {content.Columns} columns while the layout requires {Rows} rows and  {Columns} columns.");
            }
        }
    }

}