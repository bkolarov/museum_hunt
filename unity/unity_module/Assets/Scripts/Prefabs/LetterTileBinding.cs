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
        public Sprite SelectedBackground;
        public Color SelectedTextColor;
        public Sprite UnselectedBackground;
        public Color UnselectedTextColor;

        private Text TextScriptComponent;
        private SpriteRenderer SpriteRenderer;

        private Field PendingBindingFields = Field.NONE;

        private Color _TextColor;
        private Sprite _BackgroundSprite;
        private string _Letter;
        private bool _Selected = false;

        public event GameObjectClickListener OnGameObjectClick;

        public Sprite BackgroundSprite
        {
            get
            {
                return _BackgroundSprite;
            }
            set
            {
                if (_BackgroundSprite != value)
                {
                    AddPendingBindingFlag(Field.BACKGROUND_SPRITE);
                    _BackgroundSprite = value;
                }
            }
        }

        public string Letter
        {
            get
            {
                return _Letter;
            }
            set
            {
                if (string.IsNullOrEmpty(value) || value.Length != 1)
                {
                    throw new InvalidOperationException("The value must be with length 1");
                }

                if (_Letter != value)
                {
                    AddPendingBindingFlag(Field.LETTER);
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
                    AddPendingBindingFlag(Field.SELECTED);
                }
                _Selected = value;
            }
        }

        public Color TextColor
        {
            get
            {
                return _TextColor;
            }
            set
            {
                if (_TextColor != value)
                {
                    _TextColor = value;
                    AddPendingBindingFlag(Field.TEXT_COLOR);
                }
            }
        }

        // Start is called before the first frame update
        void Start()
        {
            var textGameObject = transform.Find("Canvas/Text").gameObject;
            TextScriptComponent = textGameObject.GetComponent<Text>();

            SpriteRenderer = GetComponent<SpriteRenderer>();
        }

        void Update()
        {
            if ((PendingBindingFields & Field.LETTER) == Field.LETTER)
            {
                if (TextScriptComponent != null)
                {
                    TextScriptComponent.text = _Letter;
                    ClearPendingBindingFlag(Field.LETTER);
                }
            }

            if ((PendingBindingFields & Field.SELECTED) == Field.SELECTED)
            {
                ClearPendingBindingFlag(Field.SELECTED);
            }

            if ((PendingBindingFields & Field.BACKGROUND_SPRITE) == Field.BACKGROUND_SPRITE)
            {
                SpriteRenderer.sprite = BackgroundSprite;
                ClearPendingBindingFlag(Field.BACKGROUND_SPRITE);
            }

            if ((PendingBindingFields & Field.TEXT_COLOR) == Field.TEXT_COLOR)
            {
                TextScriptComponent.color = TextColor;
                ClearPendingBindingFlag(Field.TEXT_COLOR);
            }
        }

        private void AddPendingBindingFlag(Field field)
        {
            PendingBindingFields |= field;
        }

        private void ClearPendingBindingFlag(Field field)
        {
            PendingBindingFields &= ~field;
        }

        void OnMouseDown()
        {
            Selected = !Selected;
            BackgroundSprite = Selected ? SelectedBackground : UnselectedBackground;
            TextColor = Selected ? SelectedTextColor : UnselectedTextColor;
            OnGameObjectClick?.Invoke(gameObject);
        }

        [Flags]
        private enum Field
        {
            NONE = 0,
            LETTER = 1,
            SELECTED = 2,
            BACKGROUND_SPRITE = 4,
            TEXT_COLOR = 8
        }
    }

}