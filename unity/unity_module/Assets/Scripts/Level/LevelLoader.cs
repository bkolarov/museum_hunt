using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelLoader : MonoBehaviour
{
    public GameObject TileGameObject;
    public GameObject ObstacleGameObject;

    public int TilesPerRow;
    public int TilesPerColumn;

    // Start is called before the first frame update
    void Start()
    {
        var content = new TilesLayout.TilesLayoutContent(TilesPerColumn, TilesPerRow);

        for (int col = 0; col < TilesPerColumn; col++)
        {
            for (int row = 0; row < TilesPerRow; row++)
            {
                var cell = new TilesLayout.TilesLayoutCell(TileGameObject);
                content.SetCell(cell, row, col);
            }
        }

        content.Measure();

        var tilesLayoutObject = GameObject.Find("TilesLayout");
        var tilesLayout = tilesLayoutObject.GetComponent(typeof(TilesLayout.TilesLayout)) as TilesLayout.TilesLayout;

        tilesLayout.Content = content;
    }
}
