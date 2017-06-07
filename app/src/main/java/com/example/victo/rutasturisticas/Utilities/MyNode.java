package com.example.victo.rutasturisticas.Utilities;

import com.example.victo.rutasturisticas.Domain.Node;

import java.io.Serializable;

/**
 * Created by victo on 6/6/2017.
 */

public class MyNode implements Serializable
{
    public double distance;
    public Node node;
    public MyNode nextNode;

    public MyNode(double distance, Node node)
    {
        this.distance = distance;
        this.node = node;
    }
}
