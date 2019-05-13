using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using PoqXert.MessageBox;

namespace PoqXertEditor.MessageBox
{
	public class MsgBoxStyleEditor : Editor
	{
		[MenuItem("Assets/Create/Message Style")]
		static void Create()
		{
			MSGBoxStyle style = MSGBoxStyle.CreateInstance<MSGBoxStyle>();
			AssetDatabase.CreateAsset(style, AssetDatabase.GetAssetPath(Selection.activeObject) + "/New Style.asset");
			Selection.activeObject = style;
			AssetDatabase.SaveAssets();
		}

		[MenuItem("Assets/Create/Message Style", true)]
		private static bool IsAssetAFolder()
		{			
			Object obj = Selection.activeObject;

			if (obj == null)
				return false;
			
			string path = AssetDatabase.GetAssetPath(obj.GetInstanceID());
			
			if (path.Length > 0)
			{
				return AssetDatabase.IsValidFolder(path);
			}
			
			return false;
		}
	}
}