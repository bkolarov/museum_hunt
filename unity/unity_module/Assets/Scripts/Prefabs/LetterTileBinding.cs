using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace LetterTile
{
    public class LetterTileBinding : MonoBehaviour
    {
        private Text TextScriptComponent;

        public delegate void TileOnClickListener(string letter);
        public event TileOnClickListener OnTileClick;

        public string Letter
        {
            get
            {
                AssertInitialized();
                return TextScriptComponent.text;
            }
            set
            {
                AssertInitialized();
                if (!string.IsNullOrEmpty(value) && value.Length != 1)
                {
                    throw new InvalidOperationException("The value must be with length 1");
                }
                TextScriptComponent.text = value;
            }
        }

        // Start is called before the first frame update
        void Start()
        {
            var textGameObject = transform.Find("Canvas/Text").gameObject;
            TextScriptComponent = textGameObject.GetComponent(typeof(Text)) as Text;
        }

        void OnMouseDown()
        {
            OnTileClick?.Invoke(Letter);
        }

        private void AssertInitialized()
        {
            if (TextScriptComponent == null)
            {
                throw new InvalidOperationException("Script not yet initialized.");
            }
        }
    }

}