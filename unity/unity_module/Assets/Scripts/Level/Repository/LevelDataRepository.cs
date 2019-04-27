using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Level.Repository
{
    public class LevelDataRepository : MonoBehaviour
    {
        private LevelData LevelData;

        // Start is called before the first frame update
        void Start()
        {
            var words = new List<string>
            {
                "Pesho",
                "Mariika",
                "Kircho",
                "Tisho",
                "Cecka"
            };
            LevelData = new LevelData(words);
        }

        public LevelData GetLevelData()
        {
            return LevelData;
        }
    }

    public interface ILevelDataSource
    {
        LevelData GetLevelData();
    }

    public static class LevelDataSourceFacstory
    {
        public static ILevelDataSource GetLevelDataSource()
        {
            switch (UnityEngine.Application.platform)
            {
                case RuntimePlatform.Android:
                    return new AndroidLevelDataSource();
                case RuntimePlatform.WindowsEditor:
                    return new MockLevelDataSource();
                default:
                    throw new InvalidOperationException("Platform not supported.");
            }
        }

        public class MockLevelDataSource : ILevelDataSource
        {
            public LevelData GetLevelData()
            {
                var words = new List<string>
                {
                    "Pesho",
                    "Mariika",
                    "Kircho",
                    "Tisho",
                    "Cecka"
                };
                
                return new LevelData(words);
            }
        }
    }

    public class AndroidLevelDataSource : ILevelDataSource
    {
        private static readonly string KEY_LEVEL_DATA = "level-data";

        public LevelData GetLevelData()
        {
            var UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            var currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

            var intent = currentActivity.Call<AndroidJavaObject>("getIntent");
            var extras = intent.Call<AndroidJavaObject>("getExtras");
            var hasExtra = intent.Call<bool>("hasExtra", KEY_LEVEL_DATA);

            if (!hasExtra)
            {
                throw new ReadAndroidDataException();
            }

            var levelDataJson = extras.Call<string>("getString", KEY_LEVEL_DATA);

            return JsonUtility.FromJson<LevelData>(levelDataJson);
        }

        private class ReadAndroidDataException : System.Exception
        {
            public ReadAndroidDataException() : base($"Level data json is invalid or not provided as string extra with key: {KEY_LEVEL_DATA}") {}
        }
    }
}