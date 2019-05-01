using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Util;

namespace Level.Repository
{
    public class LevelDataRepository : MonoBehaviour
    {
        private ILevelDataSource DataSource;

        private LevelData LevelData;

        // Start is called before the first frame update
        void Start()
        {
            DataSource = LevelDataSourceFacstory.GetLevelDataSource();
            LevelData = DataSource.GetLevelData();
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
    }

    internal class MockLevelDataSource : ILevelDataSource
    {
        public LevelData GetLevelData()
        {
            LevelData levelData = JsonUtility.FromJson<LevelData>("{\"HintWords\":[\"бозайник\",\"фосил\",\"минерал\",\"биология\",\"природа\"]}");
            levelData.HintWords.Print();

            return levelData;
        }
    }

    public class AndroidLevelDataSource : ILevelDataSource
    {
        private static readonly string KEY_LEVEL_DATA = "level-data";

        public LevelData GetLevelData()
        {
            var UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            var currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

            Debug.Log($"current activity instance: {currentActivity.Call<AndroidJavaObject>("getClass").Call<string>("getName")}");

            var intent = currentActivity.Call<AndroidJavaObject>("getIntent");

            Debug.Log($"obtained intent: {intent}");

            var levelDataJson = intent.Call<string>("getStringExtra", KEY_LEVEL_DATA);

            Debug.Log($"obtained level data: {levelDataJson}");

            if (levelDataJson == null)
            {
                throw new ReadAndroidDataException();
            }

            return JsonUtility.FromJson<LevelData>(levelDataJson);
        }

        private class ReadAndroidDataException : System.Exception
        {
            public ReadAndroidDataException() : base($"Level data json is invalid or not provided as string extra with key: {KEY_LEVEL_DATA}") { }
        }
    }
}