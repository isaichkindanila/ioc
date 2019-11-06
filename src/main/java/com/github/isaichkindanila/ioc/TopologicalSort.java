package com.github.isaichkindanila.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TopologicalSort {
    private final List<Node> nodes;
    private final List<BeanInfo> sorted;

    static List<BeanInfo> sorted(List<BeanInfo> beanInfoList) {
        // TODO: do topological sorting
        return new TopologicalSort(beanInfoList).sorted;
    }

    private TopologicalSort(List<BeanInfo> infoList) {
        sorted = new ArrayList<>();
        nodes = infoList.stream()
                .map(Node::new)
                .collect(Collectors.toList());

        sort();
    }

    private void sort() {
        nodes.forEach(this::visit);
    }

    private void visitChildrenOf(Node parent) {
        for (var child : parent.getBeanInfo().getArgs()) {
            for (var node : nodes) {
                if (node.getBeanInfo().getClassName().equals(child)) {
                    visit(node);
                }
            }
        }
    }

    private void visit(Node node) {
        if (node.getMark() == Mark.PERMANENT) {
            return;
        }

        if (node.getMark() == Mark.TEMPORARY) {
            throw new BeanException("cyclical dependency");
        }

        node.setMark(Mark.TEMPORARY);
        visitChildrenOf(node);
        node.setMark(Mark.PERMANENT);

        sorted.add(node.getBeanInfo());
    }

    private enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    private static class Node {
        private final BeanInfo beanInfo;
        private Mark mark;

        Node(BeanInfo beanInfo) {
            this.beanInfo = beanInfo;
            this.mark = Mark.NONE;
        }

        BeanInfo getBeanInfo() {
            return beanInfo;
        }

        Mark getMark() {
            return mark;
        }

        void setMark(Mark mark) {
            this.mark = mark;
        }
    }
}
