package com.github.isaichkindanila.ioc;

import java.util.*;
import java.util.stream.Collectors;

class TopologicalSort {
    private final List<Node> nodes;
    private final List<BeanInfo> sorted;

    static List<BeanInfo> sorted(List<BeanInfo> beanInfoList) {
        return new TopologicalSort(beanInfoList).sorted;
    }

    private TopologicalSort(List<BeanInfo> infoList) {
        // elements are only added to the beginning
        sorted = new LinkedList<>();

        nodes = infoList.stream()
                .map(Node::new)
                .collect(Collectors.toList());

        sort();
    }

    private void sort() {
        nodes.forEach(this::findParents);
        nodes.forEach(this::visit);
    }

    @SuppressWarnings("unchecked")
    private void findParents(Node child) {
        for (var argClass : child.beanInfo.getArgClasses()) {
            for (var node : nodes) {
                // if node's beanClass is subclass of argClass
                // then node is required to instantiate child
                // which means node is a 'parent'
                if (argClass.isAssignableFrom(node.beanInfo.getBeanClass())) {
                    node.children.add(child);
                }
            }
        }
    }

    private void visit(Node node) {
        if (node.mark == Mark.PERMANENT) {
            // already visited
            return;
        }

        if (node.mark == Mark.TEMPORARY) {
            // cycle found
            throw new BeanException("cyclical dependency found");
        }

        // set temporary mark to find cycles
        node.mark = Mark.TEMPORARY;

        // visit each child
        node.children.forEach(this::visit);

        // set permanent mark to avoid visiting this node in the future
        node.mark = Mark.PERMANENT;

        sorted.add(0, node.beanInfo);
    }

    private enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    private static class Node {
        private final BeanInfo beanInfo;
        private final Set<Node> children;
        private Mark mark;

        Node(BeanInfo beanInfo) {
            this.beanInfo = beanInfo;
            this.children = new HashSet<>();
            this.mark = Mark.NONE;
        }
    }
}
