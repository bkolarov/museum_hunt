using System;
using System.Collections;
using System.Collections.Generic;
using TilesLayout;
using UnityEngine;
using UnityEngine.UI;

namespace LetterTile
{
    public class LetterTileBinding : MonoBehaviour, IClickable
    {
        private Text TextScriptComponent;

        private Field PendingBindingFields = Field.NONE;

        private string _Letter;
        private bool _Selected = false;

        public event GameObjectClickListener OnGameObjectClick;

        public string Letter
        {
            get
            {
                return _Letter;
            }
            set
            {
                if (!string.IsNullOrEmpty(value) && value.Length != 1)
                {
                    throw new InvalidOperationException("The value must be with length 1");
                }

                if (_Letter != value) {
                    PendingBindingFields |= Field.LETTER;
                }

                // Probably not the best place to convert it to upper case but 
                // there is no reason to over-complicate the code. I set it here as I 
                // want the view to decide whether to show upper or lower case letters.
                _Letter = value.ToUpper();
            }
        }

        public bool Selected
        {
            get
            {
                return _Selected;
            }
            set
            {
                if (_Selected != value)
                {
                    PendingBindingFields |= Field.SELECTED;
                }
                _Selected = value;
            }
        }

        // Start is called before the first frame update
        void Start()
        {
            var textGameObject = transform.Find("Canvas/Text").gameObject;
            TextScriptComponent = textGameObject.GetComponent(typeof(Text)) as Text;
        }

        void Update()
        {
            if ((PendingBindingFields & Field.LETTER) == Field.LETTER)
            {
                if (TextScriptComponent != null)
                {
                    TextScriptComponent.text = _Letter;
                    PendingBindingFields &= ~Field.LETTER;
                }
            }

            if ((PendingBindingFields & Field.SELECTED) == Field.SELECTED)
            {
                // TODO - Update the game object when we support selection.
                PendingBindingFields &= ~Field.SELECTED;
            }
        }

        void OnMouseDown()
        {
            Selected = !Selected;
            //            OnTileClick?.Invoke(Letter);
            OnGameObjectClick?.Invoke(gameObject);
        }

        [Flags]
        private enum Field
        {
            NONE = 0,
            LETTER = 1,
            SELECTED = 2
        }
    }

}