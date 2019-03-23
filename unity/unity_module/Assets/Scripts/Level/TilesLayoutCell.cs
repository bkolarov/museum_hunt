using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace TilesLayout
{
    public class TilesLayoutCell
    {
        public GameObject GameObject { get; set; }

        public float MarginLeft { get; set; }
        public float MarginTop { get; set; }
        public float MarginRight { get; set; }
        public float MarginBottom { get; set; }

        public TilesLayoutCell(GameObject gameObject)
        {
            GameObject = gameObject;
            // TODO - make a TilesLayoutCell prefab and set those from the Unity editor.
            MarginLeft = 0.1f;
            MarginRight = 0.1f;
            MarginTop = 0.1f;
            MarginBottom = 0.1f;
        }

        public float MeasuredWidth
        {
            get
            {
                float GameObjectWidth;
                if (GameObject == null)
                {
                    GameObjectWidth = 1;
                }
                else
                {
                    GameObjectWidth = GameObject.transform.localScale.x;
                }

                return GameObjectWidth + MarginLeft + MarginRight;
            }
        }

        public float MeasuredHeight
        {
            get
            {
                float GameObjectHeight;
                if (GameObject == null)
                {
                    GameObjectHeight = 1;
                } else
                {
                    GameObjectHeight = GameObject.transform.localScale.y;
                }

                return GameObjectHeight + MarginTop + MarginBottom;
            }
        }
    }
}