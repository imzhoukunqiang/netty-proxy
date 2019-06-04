package top.zkq.algorithm.tree.binary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class NodePanel extends JPanel {

    public static final JComponent EMTPY = new JPanel();
    private JComponent l;
    private JComponent r;
    private JPanel children;
    private JLabel textLabel;

    public NodePanel(String text) {
        super(new GridLayout(2, 1));
        this.setBorder(new LineBorder(Color.BLACK));
        this.textLabel = new JLabel(text,SwingConstants.CENTER);
        textLabel.setSize(10,10);
        this.add(textLabel);

        children = new JPanel(new GridLayout(1, 2));
        children.add(l == null ? EMTPY : l);
        children.add(r == null ? EMTPY : r);
        this.add(children);
    }


    public NodePanel setL(NodePanel l) {
        children.remove(l);
        this.l = l;
        children.add(l, 0);
        return this;
    }

    public NodePanel setR(NodePanel r) {
        children.remove(r);
        this.r = r;
        children.add(r, 2);
        return this;
    }

    public NodePanel setTextLabel(String text) {
        this.textLabel.setText(text);
        return this;
    }
}