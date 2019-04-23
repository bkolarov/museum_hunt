using UnityEngine;
using System.Collections;

namespace PoqXert.MessageBox
{
	public class MSGBoxStyle : ScriptableObject
	{
		//Цвет заголовка
		[SerializeField]
		[TooltipAttribute("Color of caption")]
		private Color _captionColor = Color.blue;
		public Color captionColor
		{
			get{return _captionColor;}
			set{_captionColor = value;}
		}
		//Цвет фона
		[SerializeField]
		[TooltipAttribute("Color of background")]
		private Color _backgroundColor = Color.white;
		public Color backgroundColor
		{
			get{return _backgroundColor;}
			set{_backgroundColor = value;}
		}
		//Цвет кнопки "Да"/"Ок"
		[SerializeField]
		[TooltipAttribute("Color of button \"Yes/Ok\"")]
		private Color _btnYesColor = Color.green;
		public Color btnYesColor
		{
			get{return _btnYesColor;}
			set{_btnYesColor = value;}
		}
		//Цвет кнопки "Нет"
		[SerializeField]
		[TooltipAttribute("Color of button \"No\"")]
		private Color _btnNoColor = Color.blue;
		public Color btnNoColor
		{
			get{return _btnNoColor;}
			set{_btnNoColor = value;}
		}
		//Цвет кнопки "Отмена"
		[SerializeField]
		[TooltipAttribute("Color of button \"Cancel\"")]
		private Color _btnCancelColor = Color.red;
		public Color btnCancelColor
		{
			get{return _btnCancelColor;}
			set{_btnCancelColor = value;}
		}
		//Иконка
		[SerializeField]
		[TooltipAttribute("Icon for MessageBox")]
		private Sprite _icon = null;
		public Sprite icon
		{
			get{return _icon;}
			set{_icon = value;}
		}
	}
}