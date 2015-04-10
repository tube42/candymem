package se.tube42.kidsmem.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.service.*;

import se.tube42.kidsmem.data.*;


public class BackgroundMesh extends Item implements TweenListener
{
    private Mesh mesh;
    private float [] vertices;

    public BackgroundMesh()
    {
        super(3 * 4); // used for 4 * RGB

        vertices = new float[4 * 3];

        mesh = new Mesh(true, 6, 4,
                  new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                  new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color")
                  );
        mesh.setIndices(new short[] { 0, 1, 2, 3 });

        setStaticColors(0, 0, 0, 0);
    }

    // ------------------------------------------------------------------

    public void setStaticColors(float time, float r, float g, float b)
    {
        for(int i = 0; i < 4; i++) {
            set(i * 3 + 0, r).configure(time, null);
            set(i * 3 + 1, g).configure(time, null);
            set(i * 3 + 2, b).configure(time, null);
        }
    }

    public void startRandomColors()
    {
        for(int i = 0; i < 4; i++)
            set_random_color(i);
    }

    // ------------------------------------------------------------------

    public void setPos(int index, int x, int y)
    {
        vertices[index * 3 + 0] = x;
        vertices[index * 3 + 1] = y;
    }

    public void resize(int w, int h)
    {
        setPos(0, 0, 0);
        setPos(1, w, 0);
        setPos(2, w, h);
        setPos(3, 0, h);
    }

    // ------------------------------------------------------------------

    public void draw(Camera camera)
    {

        // updated edge colors if one component has chaned
        for(int e = 0; e < 4; e++) {
            vertices[e * 3 + 2] = Color.toFloatBits(
                      get(e * 3 + 0),
                      get(e * 3 + 1),
                      get(e * 3 + 2), 1);
        }


        mesh.setVertices(vertices);

        final ShaderProgram shader = Assets.shader_col2;
        shader.begin();
        shader.setUniformMatrix("u_projTrans", camera.combined);
        mesh.render(shader, GL20.GL_TRIANGLE_FAN, 0, 4);
        shader.end();
    }


    private void set_random_color(int col)
    {
        if(col < 0 || col >= 4) return; // ??


        final float t = RandomService.get(4, 7);
        final float p = RandomService.get(3, 5);

        final float r = RandomService.get(0.5f, 0.95f);
        final float g = RandomService.get(0.5f, 0.95f);
        final float b = RandomService.get(0.5f, 0.95f);

        set(col * 3 + 0, r).configure(t, null);
        set(col * 3 + 1, g).configure(t, null);
        set(col * 3 + 2, b).configure(t, null).pause(p).finish(this, col);

    }
    public void onFinish(Item item, int index, int msg)
    {
        set_random_color(msg);
    }
}
