//Created by PoqXert (poqxert@gmail.com)

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine.EventSystems;

namespace PoqXert.MessageBox
{
    public class MsgBox : MonoBehaviour
    {
        //Главная панел
        [SerializeField]
        private RectTransform _mainPanel;
        //Панель для блокирования других GUI
        [SerializeField]
        private GameObject _blockPanel;
        //Заголовок
        [SerializeField]
        private Text _captionText;
        //Сообщение
        [SerializeField]
        private Text _mainText;
        /* Оформление */
        //Заголовок
        [SerializeField]
        private Image _captionImg;
        //Фон
        [SerializeField]
        private Image _backgroundImg;
        //Иконка
        [SerializeField]
        private Image _iconImg;
        //Кнопка "Да"/"Ок"
        [SerializeField]
        private Image _btnYesImg;
        //Кнопка "Нет"
        [SerializeField]
        private Image _btnNoImg;
        //Кнопка "Отмена"
        [SerializeField]
        private Image _btnCancelImg;
        //EventSystem
        [SerializeField]
        private GameObject _eventer;
        //Стили
        [SerializeField]
        private MSGBoxStyle _informationStyle = null;
        [SerializeField]
        private MSGBoxStyle _questionStyle = null;
        [SerializeField]
        private MSGBoxStyle _warningStyle = null;
        [SerializeField]
        private MSGBoxStyle _errorStyle = null;
        [SerializeField]
        private List<MSGBoxStyle> _customStyles = new List<MSGBoxStyle>();

        private static MsgBox _prefab = null;
        private static MsgBox prefab
        {
            get
            {
                if (_prefab == null)
                    _prefab = Resources.Load<MsgBox>("PXMSG/MSG");
                return _prefab;
            }
        }
        //Последнее окно сообщения
        private static MsgBox _lastMsgBox;
        /// <summary>
        /// Gets the open MessageBox.
        /// </summary>
        /// <value>The MessageBox.</value>
        public static MsgBox instance
        {
            get { return _lastMsgBox; }
        }
        /// <summary>
        /// MessageBox is open?
        /// </summary>
        /// <value><c>true</c> if is open; otherwise, <c>false</c>.</value>
        public static bool isOpen
        {
            get { return _lastMsgBox != null; }
        }
        /// <summary>
        /// MessageBox is modal (blocked other UI elements)?
        /// </summary>
        /// <value><c>true</c> if is modal; otherwise, <c>false</c>.</value>
        public static bool isModal
        {
            get
            {
                return isOpen && _lastMsgBox._blockPanel.activeSelf;
            }
        }
        //Message Box ID
        private int messageID = 0;

        private DialogResultMethod calledMethod;

        void ButtonClickEvent(int btn)
        {
            calledMethod(messageID, (DialogResult)btn);
        }

        /// <summary>
        /// Генерация окна сообщения.
        /// </summary>
        /// <param name="mess">Сообщение.</param>
        /// <param name="caption">Заголовок.</param>
        /// <param name="btns">Кнопки.</param>
        /// <param name="style">Стиль.</param>
        /// <param name="btnText0">Текст кнопки "Да"/"Ок".</param>
        /// <param name="btnText1">Текст кнопки "Нет".</param>
        /// <param name="btnText2">Текст кнопки "Отмена".</param>
        private void BuildMessageBox(int id, string mess, string caption, MsgBoxButtons btns, MSGBoxStyle style, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {

            //Сохраняем ID
            messageID = id;
            //Статус блокировки других GUI
            _blockPanel.SetActive(modal);
            //Сохранение метода
            calledMethod = method;
            //Устанавливаем заголовок
            this._captionText.text = caption;
            //Устанавливаем сообщение
            this._mainText.text = mess;
            //Устанавливаем цвет заголовка
            this._captionImg.color = style.captionColor;
            //Устанавливаем цвет фона
            this._backgroundImg.color = style.backgroundColor;
            //Устанавливаем цвет кнопок
            this._btnYesImg.color = style.btnYesColor;
            this._btnNoImg.color = style.btnNoColor;
            this._btnCancelImg.color = style.btnCancelColor;
            //Устанавливаем иконку
            this._iconImg.sprite = style.icon;
            //Устанавливаем размер
            Vector2 mainSize = new Vector2(Mathf.Clamp(Mathf.Max(_captionText.preferredWidth, 150 + _mainText.preferredWidth), 256, Screen.width), Mathf.Clamp(_mainText.preferredHeight, 256, Screen.height));
            //this._mainPanel.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, mainSize.x);
            //this._mainPanel.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y);

            RectTransform btnTr;
            Text btnText;
            float cancelW;
            float noW;
            //Устанавливаем нужные кнопки
            switch (btns)
            {
                //Кнопка "Ок"
                case MsgBoxButtons.OK:
                    //Получаем рект кнопки "Ок"
                    btnTr = _btnYesImg.rectTransform;
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-25.0f, -46.46f);
                    //Получаем элемент "текст"
                    btnText = _btnYesImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText0 == "") ? "Ok" : btnText0;
                    //Устанавливаем размер
                   // btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, Mathf.Clamp(btnText.preferredWidth + 20, 70, mainSize.x / 2 - 30));
                    //btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Отключаем кнопку "Нет"
                    _btnNoImg.gameObject.SetActive(false);
                    //Отключаем кнопку "Отмена"
                    _btnCancelImg.gameObject.SetActive(false);
                    break;
                //Кнопки "Ок" и "Отмена"
                case MsgBoxButtons.OK_CANCEL:
                    //Получаем элемент "текст"
                    btnText = _btnCancelImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText2 == "") ? "Cancel" : btnText2;
                    //Получаем рект кнопки "Отмена"
                    btnTr = _btnCancelImg.rectTransform;
                    //Устанавливаем размер
                    cancelW = Mathf.Clamp(btnText.preferredWidth + 20, 70, mainSize.x / 2 - 30);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, cancelW);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-25.0f, -46.46f);
                    //Получаем рект кнопки "Ок"
                    btnTr = _btnYesImg.rectTransform;
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-(20.0f + cancelW), -46.46f);
                    //Получаем элемент "текст"
                    btnText = _btnYesImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText0 == "") ? "Ok" : btnText0;
                    //Устанавливаем размер
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, Mathf.Clamp(btnText.preferredWidth + 20, 70, mainSize.x / 2 - 30));
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Отключаем кнопку "Нет"
                    _btnNoImg.gameObject.SetActive(false);
                    break;
                //Кнопки "Да" и "Нет"
                case MsgBoxButtons.YES_NO:
                    //Получаем элемент "текст"
                    btnText = _btnNoImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText1 == "") ? "No" : btnText1;
                    //Получаем рект кнопки "Нет"
                    btnTr = _btnNoImg.rectTransform;
                    //Устанавливаем размер
                    noW = Mathf.Clamp(btnText.preferredWidth + 20, 70, mainSize.x / 2 - 30);
                    //btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, noW);
                    //btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-25.0f, -46.46f);
                    //Получаем рект кнопки "Ок"
                    btnTr = _btnYesImg.rectTransform;
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-165, -46.46f);
                    //Получаем элемент "текст"
                    btnText = _btnYesImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText0 == "") ? "Yes" : btnText0;
                    //Устанавливаем размер
                    //btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, Mathf.Clamp(btnText.preferredWidth + 20, 70, mainSize.x / 2 - 30));
                    //btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Отключаем кнопку "Отмена"
                    _btnCancelImg.gameObject.SetActive(false);
                    break;
                case MsgBoxButtons.YES_NO_CANCEL:
                    //Получаем элемент "текст"
                    btnText = _btnCancelImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText2 == "") ? "Cancel" : btnText2;
                    //Получаем рект кнопки "Отмена"
                    btnTr = _btnCancelImg.rectTransform;
                    //Устанавливаем размер
                    cancelW = Mathf.Clamp(btnText.preferredWidth + 20, 35, mainSize.x / 3 - 30);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, cancelW);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-25.0f, -46.46f);
                    //Получаем элемент "текст"
                    btnText = _btnNoImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText1 == "") ? "No" : btnText1;
                    //Получаем рект кнопки "Нет"
                    btnTr = _btnNoImg.rectTransform;
                    //Устанавливаем размер
                    noW = Mathf.Clamp(btnText.preferredWidth + 20, 35, mainSize.x / 3 - 30);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, noW);
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    btnTr.anchoredPosition = new Vector2(-(20.0f + cancelW), -46.46f);
                    //Получаем рект кнопки "Ок"
                    btnTr = _btnYesImg.rectTransform;
                    //Устанавливаем позицию
                    btnTr.anchoredPosition = new Vector2(-(30.0f + cancelW + noW), -46.46f);
                    //Получаем элемент "текст"
                    btnText = _btnYesImg.GetComponentInChildren<Text>();
                    //Устанавливаем текст
                    btnText.text = (btnText0 == "") ? "Ok" : btnText0;
                    //Устанавливаем размер
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, Mathf.Clamp(btnText.preferredWidth + 20, 35, mainSize.x / 3 - 30));
                    btnTr.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, mainSize.y * 0.2f);
                    break;
                default:
                    break;
            }
        }

        public static MSGBoxStyle GetStyle(MsgBoxStyle id)
        {
            switch (id)
            {
                case MsgBoxStyle.Information:
                    return prefab._informationStyle;
                case MsgBoxStyle.Question:
                    return prefab._questionStyle;
                case MsgBoxStyle.Warning:
                    return prefab._warningStyle;
                case MsgBoxStyle.Error:
                    return prefab._errorStyle;
                default:
                    return prefab._customStyles[0];
            }
        }

        /// <summary>
        /// Show the message box.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        public static void Show(int id, string message, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            Show(id, message, "", MsgBoxButtons.OK, MsgBoxStyle.Information, method, modal, btnText0, btnText1, btnText2);
        }

        /// <summary>
        /// Show the message box with caption.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="caption">Caption.</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        public static void Show(int id, string message, string caption, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            Show(id, message, caption, MsgBoxButtons.OK, MsgBoxStyle.Information, method, modal, btnText0, btnText1, btnText2);
        }

        /// <summary>
        /// Show the message box with caption.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="caption">Caption.</param>
        /// <param name="buttons">Buttons.</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        public static void Show(int id, string message, string caption, MsgBoxButtons buttons, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            Show(id, message, caption, buttons, MsgBoxStyle.Information, method, modal, btnText0, btnText1, btnText2);
        }

        /// <summary>
        /// Show the message box with caption.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="caption">Caption.</param>
        /// <param name="buttons">Buttons.</param>
        /// <param name="style">Message Box Style.</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        public static void Show(int id, string message, string caption, MsgBoxButtons buttons, MsgBoxStyle style, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            Show(id, message, caption, buttons, GetStyle(style), method, modal, btnText0, btnText1, btnText2);
        }

        /// <summary>
        /// Show the message box with caption and buttons.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="caption">Caption.</param>
        /// <param name="buttons">Buttons.</param>
        /// <param name="style">Style of message box</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        private static void Show(int id, string message, string caption, MsgBoxButtons buttons, MSGBoxStyle style, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            Close();
            _lastMsgBox = Instantiate(prefab);
            _lastMsgBox.BuildMessageBox(id, message, caption, buttons, style, method, modal, btnText0, btnText1, btnText2);
            if (EventSystem.current == null)
                _lastMsgBox._eventer.SetActive(true);
        }

        /// <summary>
        /// Show the message box with caption.
        /// </summary>
        /// <param name="id">Message Box ID</param>
        /// <param name="message">Message.</param>
        /// <param name="caption">Caption.</param>
        /// <param name="buttons">Buttons.</param>
        /// <param name="customStyleID">ID custom style.</param>
        /// <param name="method">Called method with result</param>
        /// <param name="modal">if <c>true</c> then blocked other GUI elements</param>
        /// <param name="btnText0">Text for button Yes/Ok. "" - use default value</param>
        /// <param name="btnText1">Text for button No. "" - use default value</param>
        /// <param name="btnText2">Text for button Cancel. "" - use default value</param>
        public static void Show(int id, string message, string caption, MsgBoxButtons buttons, int customStyleID, DialogResultMethod method, bool modal = false, string btnText0 = "", string btnText1 = "", string btnText2 = "")
        {
            if (customStyleID < 0 || customStyleID >= prefab._customStyles.Count)
            {
                Debug.LogError("Custom style not found by ID");
                return;
            }
            Show(id, message, caption, buttons, prefab._customStyles[customStyleID], method, modal, btnText0, btnText1, btnText2);
        }

        /// <summary>
        /// Close the last message box.
        /// </summary>
        public static void Close()
        {
            if (_lastMsgBox != null)
            {
                DestroyImmediate(_lastMsgBox.gameObject);
            }
        }

        public class Builder
        {
            public int id;
            public string message;
            public string caption;
            public MsgBoxButtons buttons;
            public MsgBoxStyle style = MsgBoxStyle.Information;
            public DialogCallback positiveCallback;
            public DialogCallback negativeCallback;
            public DialogCallback neutralCallback;
            public bool modal = false;
            public string positiveButtonText = "";
            public string negativeButtonText = "";
            public string cancelButtonText = "";

            public void Show()
            {
                MsgBox.Show(id, message, caption, buttons, style, (id, result) => {
                    switch (result)
                    {
                        case DialogResult.CANCEL:
                            neutralCallback?.Invoke(id);
                            break;
                        case DialogResult.NO:
                            negativeCallback?.Invoke(id);
                            break;
                        case DialogResult.YES_OK:
                            positiveCallback?.Invoke(id);
                            break;
                    }
                }, modal, positiveButtonText, negativeButtonText, cancelButtonText);
            }
        }
    }

    public delegate void DialogCallback(int dialogId);

    public delegate void DialogResultMethod(int id, DialogResult result);

    public enum DialogResult
    {
        YES_OK = 0,
        NO = 1,
        CANCEL = 2
    }

    public enum MsgBoxStyle
    {
        Information = 0,
        Question = 1,
        Warning = 2,
        Error = 3,
        Custom = 4
    }

    public enum MsgBoxButtons
    {
        OK = 0,
        OK_CANCEL = 1,
        YES_NO = 2,
        YES_NO_CANCEL = 3
    }
}