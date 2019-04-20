using System.Collections;
using System.Collections.Generic;
using TilesLayout;
using UnityEngine;
using UnityEngine.UI;

namespace UI
{
    public class TextPanelBinding : MonoBehaviour, IClickable
    {
        private Text TextScriptCompnent;

        public event GameObjectClickListener OnGameObjectClick;

        private Field PendingBindingFields = Field.NONE;

        private string _Text;
        public string Text
        {
            get
            {
                return _Text;
            }

            set
            {
                _Text = value;
                PendingBindingFields |= Field.TEXT;
            }
        }

        // Start is called before the first frame update
        void Start()
        {
            TextScriptCompnent = transform.Find("Text").GetComponent<Text>();
        }

        // Update is called once per frame
        void Update()
        {
            OnGameObjectClick?.Invoke(gameObject);

            if ((PendingBindingFields & Field.TEXT) == Field.TEXT)
            {
                TextScriptCompnent.text = _Text;
                PendingBindingFields &= ~Field.TEXT;
            }
        }

        private enum Field
        {
            NONE = 0,
            TEXT = 1
        }
    }
}
