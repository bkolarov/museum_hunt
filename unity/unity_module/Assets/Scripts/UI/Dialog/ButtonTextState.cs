using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace UI.Dialog
{
    public class ButtonTextState : MonoBehaviour, IPointerDownHandler, IPointerUpHandler, IPointerClickHandler, IPointerEnterHandler, IPointerExitHandler
    {

        public int IdleSize;
        public int PressedSize;

        private Button Button;
        private Text Text;

        private int TextSize
        {
            get
            {
                return Text.fontSize;
            }
            set
            {
                if (Text != null && Text.fontSize != value)
                {
                    Text.fontSize = value;
                }
            }
        }

        private bool pressed;

        // Start is called before the first frame update
        void Start()
        {
            Button = GetComponentInParent<Button>();
            Text = GetComponent<Text>();
        }

        // Update is called once per frame
        void Update()
        {
            if (pressed)
            {
                Text.fontSize = PressedSize;
            }
            else
            {
                Text.fontSize = IdleSize;
            }
        }

        public void OnPointerDown(PointerEventData eventData)
        {
            Button.OnPointerDown(eventData);
            pressed = true;
        }

        public void OnPointerUp(PointerEventData eventData)
        {
            Button.OnPointerUp(eventData);
            pressed = false;
        }

        public void OnPointerClick(PointerEventData eventData)
        {
            Button.OnPointerClick(eventData);
        }

        public void OnPointerEnter(PointerEventData eventData)
        {
            Button.OnPointerEnter(eventData);
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            Button.OnPointerExit(eventData);
        }
    }
}
