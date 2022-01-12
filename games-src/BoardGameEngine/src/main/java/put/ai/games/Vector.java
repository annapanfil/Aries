/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games;

import java.util.Objects;

public class Vector {

    private final int srcX, srcY, dstX, dstY;


    public Vector(int srcX, int srcY, int dstX, int dstY) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
    }


    public int getSrcX() {
        return srcX;
    }


    public int getSrcY() {
        return srcY;
    }


    public int getDstX() {
        return dstX;
    }


    public int getDstY() {
        return dstY;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector other = (Vector) obj;
        return other.srcX == this.srcX && other.srcY == this.srcY && other.dstX == this.dstX && other.dstY == this.dstY;
    }


    @Override
    public int hashCode() {
        return Objects.hash(srcX, srcY, dstX, dstY);
    }

}
