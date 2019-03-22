using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TilesLayout : MonoBehaviour
{
    public int tilesPerRow = 0;

    public int tilesPerColumn = 0;

    public float paddingLeft = 0;

    public float paddingRight = 0;

    public GameObject tile;

    private List<List<GameObject>> grid;

    // Start is called before the first frame update
    void Start()
    {
        var screenAspect = (float) Screen.width / Screen.height;
        var camWidth = screenAspect * 2f * Camera.main.orthographicSize;

        var desiredWidth = tilesPerRow + paddingLeft + paddingRight;
        Camera.main.orthographicSize = desiredWidth / (screenAspect * 2f);

        var newTile = (GameObject)Instantiate(tile, transform);

        //Debug.Log(newTile);
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void AddGameObject(GameObject game)
    {

    }
}
