using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace PlatformApplication
{
    public static class MobileApplication
    {

        public static void Quit(System.Object result = null)
        {
            switch (Application.platform)
            {
                case RuntimePlatform.Android:
                    AndroidAppliication.Quit(result);
                    break;
                case RuntimePlatform.WindowsEditor:
                    Application.Quit();
                    break;
                default:
                    throw new InvalidOperationException("Platform not supported");
            }
        }
    }

    internal static class AndroidAppliication
    {
        public static void Quit(System.Object result = null)
        {
            var UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            var currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

            var resultJson = JsonUtility.ToJson(result, true);

            AndroidJavaClass intent = null;
            if (result != null)
            {
                currentActivity.Call("finishWithResult", resultJson);
            }

            UnityEngine.Application.Quit();
        }
    }


}
