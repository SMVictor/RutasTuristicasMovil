package com.example.victo.rutasturisticas.Utilities;

import com.example.victo.rutasturisticas.Domain.Node;

import java.io.Serializable;

/**
 * Created by victo on 6/6/2017.
 */

public class MyLinkedList implements Serializable {

    public MyNode raiz;
    private int counter;

    public MyLinkedList() {
    }

    public boolean isEmpty()
    {
        if(counter==0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public  int size()
    {
        return counter;
    }
    public void orderedInsert(double distance, Node node)
    {
        counter++;

        if(raiz==null)
        {
            MyNode newNode = new MyNode(distance, node);
            raiz=newNode;
        }
        else
        {
            if(distance<=raiz.distance)
            {
                MyNode newNode = new MyNode(distance, node);
                newNode.nextNode = raiz;
                raiz = newNode;
            }
            else
            {
                MyNode aux = raiz;
                MyNode newNode = new MyNode(distance, node);

                while(aux.nextNode!=null)
                {
                    if(distance<=aux.nextNode.distance)
                    {
                        newNode.nextNode = aux.nextNode;
                        aux.nextNode = newNode;
                        break;
                    }
                    else
                    {
                        aux=aux.nextNode;
                    }
                }
                if(aux.nextNode==null)
                {
                   aux.nextNode = newNode;
                }
            }
        }
    }
    public void cut(int point)
    {
        int count = 0;
        MyNode aux= raiz;

        if(raiz!=null)
        {
            while (count<point && aux.nextNode!=null)
            {
                aux=aux.nextNode;
                count+=1;
            }
            aux.nextNode=null;
            counter=count;
        }
    }
    public MyNode getNode(int position)
    {
        int count = 0;
        MyNode aux= raiz;

        while (count<position && aux!=null)
        {
            aux=aux.nextNode;
            count+=1;
        }
        MyNode newNode = new MyNode(aux.distance, aux.node);
        return newNode;
    }
}
