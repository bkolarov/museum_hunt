using UnityEngine;

public class MovementController : MonoBehaviour
{
    public float SpeedX = 5.0f;

    private float raycastDistance = 0.0f;
    private ConstantForce2D constantForce2D;
    private SpriteRenderer SpriteRenderer;

    void Start()
    {

        SpriteRenderer = GetComponent<SpriteRenderer>();
        raycastDistance = transform.localScale.x * GetComponent<BoxCollider2D>().size.x;

        constantForce2D = GetComponent<ConstantForce2D>();
        constantForce2D.relativeForce = new Vector2(SpeedX, 0.0f);

    }

    void FixedUpdate()
    {
        RaycastHit2D hitRight = Physics2D.Raycast(GetComponent<Renderer>().bounds.center, Vector2.right, raycastDistance);
        if (hitRight.collider != null)
        {
        	constantForce2D.relativeForce = new Vector2(-SpeedX, 0.0f);
            SpriteRenderer.flipX = true;
        }

        RaycastHit2D hitLeft = Physics2D.Raycast(GetComponent<Renderer>().bounds.center, Vector2.left, raycastDistance);
        if (hitLeft.collider != null)
        {
        	constantForce2D.relativeForce = new Vector2(SpeedX, 0.0f);
            SpriteRenderer.flipX = false;
        }
    }
}
