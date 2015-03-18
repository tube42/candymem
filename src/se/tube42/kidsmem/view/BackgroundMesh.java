package se.tube42.kidsmem.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;

import se.tube42.kidsmem.data.*;


public class BackgroundMesh extends Item implements TweenListener
{
    private Mesh mesh;
    private float [] vertices;

    private boolean dirty;
    private int  dirty_items;

    public BackgroundMesh()
    {
        super(3 * 4);
        
        dirty = true;
        dirty_items = 0;
        vertices = new float[4 * 3];
        
        mesh = new Mesh(true, 6, 4,
                  new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                  new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color")
                  );        
        mesh.setIndices(new short[] { 0, 1, 2, 3 });
    }
    
    // ------------------------------------------------------------------

    public void setEdgeColor(int edge, float r, float g, float b)
    {
        setImmediate(edge * 3 + 0, r);
        setImmediate(edge * 3 + 1, g);
        setImmediate(edge * 3 + 2, b);
        
        dirty_items |= 1 << (edge * 3);
    }
    
    public void setEdgeColor(int edge, float time, float r, float g, float b)
    {
        set(edge * 3 + 0, r).configure(time, null).finish(this, edge * 3 + 0);
        set(edge * 3 + 1, g).configure(time, null).finish(this, edge * 3 + 1);
        set(edge * 3 + 2, b).configure(time, null).finish(this, edge * 3 + 2);
    }
    
    // ------------------------------------------------------------------
    
    public void setPos(int index, int x, int y)
    {
        vertices[index * 3 + 0] = x;
        vertices[index * 3 + 1] = y;
        dirty = true;
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
        // check if tweens have been updated:
        for(int i = 0; i < 3 * 4; i++) {
            if(isTweenActive(i)) {
                dirty_items |= 1 << i;
            }
        }
        
        // updated edge colors if one component has chaned
        for(int e = 0; e < 4; e++) {
            if( (dirty_items & 7) != 0) {
                vertices[e * 3 + 2] = Color.toFloatBits(
                          get(e * 3 + 0),
                          get(e * 3 + 1),
                          get(e * 3 + 2), 1);
                dirty = true;        
            }
            dirty_items >>= 3;
        }
        
        // update vertices?
        if(dirty) {
            dirty = false;
            mesh.setVertices(vertices);
        }

        
        final ShaderProgram shader = Assets.shader_col2;
        shader.begin();
        shader.setUniformMatrix("u_projTrans", camera.combined);
        mesh.render(shader, GL20.GL_TRIANGLE_FAN, 0, 4);
        shader.end();
    }
    
    
    public void onFinish(Item item, int index, int msg)
    {
        // this forces us to get the last & final value from the tween
        dirty_items |= 1 << index;        
    }
}
