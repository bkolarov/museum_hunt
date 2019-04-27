using System;
using System.Collections;
using System.Collections.Generic;
using TilesLayout;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace LetterTile
{
    public class LetterTileBinding : MonoBehaviour, IClickable, IPointerClickHandler
    {
        public Sprite SelectedBackground;
        public Color SelectedTextColor;
        public Sprite UnselectedBackground;
        public Color UnselectedTextColor;
        public Sprite ErrorBackground;
        public Color ErrorTextColor;

        private Text TextScriptComponent;
        private SpriteRenderer SpriteRenderer;

        private Field PendingBindingFields = Field.NONE;

        private Color _TextColor;
        private string _Letter;

        public event GameObjectClickListener OnGameObjectClick;

        private bool TileActive
        {
            get
            {
                bool active;
                switch (TileState)
                {
                    case State.INACTIVE:
                        active = false;
                        break;
                    case State.IDLE:
                    case State.SELECTED:
                    case State.ERROR:
                    default:
                        active = true;
                        break;
                }

                return active;
            }
        }

        public Sprite BackgroundSprite
        {
            get
            {
                Sprite sprite;
                switch (TileState)
                {
                    case State.SELECTED:
                        sprite = SelectedBackground;
                        break;
                    case State.ERROR:
                        sprite = ErrorBackground;
                        break;
                    case State.IDLE:
                    default:
                        sprite = UnselectedBackground;
                        break;
                }

                return sprite;
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

        private State _TileState;

        public State TileState
        {
            get
            {
                return _TileState;
            }
            set
            {
                if (_TileState != value)
                {
                    _TileState = value;
                    AddPendingBindingFlag(Field.STATE);
                    AddPendingBindingFlag(Field.ACTIVE);
                    AddPendingBindingFlag(Field.BACKGROUND_SPRITE);
                    AddPendingBindingFlag(Field.TEXT_COLOR);
                }
            }
        }

        public Color TextColor
        {
            get
            {
                Color color;
                switch (TileState)
                {
                    case State.SELECTED:
                        color = SelectedTextColor;
                        break;
                    case State.ERROR:
                        color = ErrorTextColor;
                        break;
                    case State.IDLE:
                    default:
                        color = UnselectedTextColor;
                        break;
                }
                return color;
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
            if (HasPendingBindingFor(Field.LETTER))
            {
                if (TextScriptComponent != null)
                {
                    TextScriptComponent.text = _Letter;
                    ClearPendingBindingFlag(Field.LETTER);
                }
            }

            if (HasPendingBindingFor(Field.STATE))
            {
                ClearPendingBindingFlag(Field.STATE);
            }

            if (HasPendingBindingFor(Field.BACKGROUND_SPRITE))
            {
                SpriteRenderer.sprite = BackgroundSprite;
                ClearPendingBindingFlag(Field.BACKGROUND_SPRITE);
            }

            if (HasPendingBindingFor(Field.TEXT_COLOR))
            {
                TextScriptComponent.color = TextColor;
                ClearPendingBindingFlag(Field.TEXT_COLOR);
            }

            if (HasPendingBindingFor(Field.ACTIVE))
            {
                gameObject.SetActive(TileActive);
                ClearPendingBindingFlag(Field.ACTIVE);
            }
        }

        private bool HasPendingBindingFor(Field field)
        {
            return (PendingBindingFields & field) == field;
        }

        private void AddPendingBindingFlag(Field field)
        {
            PendingBindingFields |= field;
        }

        private void ClearPendingBindingFlag(Field field)
        {
            PendingBindingFields &= ~field;
        }

        public void OnPointerClick(PointerEventData eventData)
        {
            OnGameObjectClick?.Invoke(gameObject);
        }

        [Flags]
        private enum Field
        {
            NONE = 0,
            LETTER = 1,
            STATE = 2,
            BACKGROUND_SPRITE = 4,
            TEXT_COLOR = 8,
            ACTIVE = 16
        }

        public enum State
        {
            IDLE, SELECTED, ERROR, INACTIVE
        }
    }

}