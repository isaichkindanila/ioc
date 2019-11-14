package com.github.isaichkindanila.ioc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class TopologicalSort {
    private final List<Node> nodes;
    private final List<ComponentInfo> sorted;

    private TopologicalSort(List<ComponentInfo> infoList) {
        // elements are only added to the beginning
        sorted = new LinkedList<>();

        nodes = infoList.stream()
                .map(Node::new)
                .collect(Collectors.toList());

        sort();
    }

    static List<ComponentInfo> sorted(List<ComponentInfo> componentInfoList) {
        return new TopologicalSort(componentInfoList).sorted;
    }

    private void sort() {
        nodes.forEach(this::findDependencies);
        nodes.forEach(this::visit);
    }

    @SuppressWarnings("unchecked")
    private void findDependencies(Node node) {
        for (var argClass : node.componentInfo.getArgClasses()) {
            for (var n : nodes) {
                // if n's beanClass is subclass of argClass
                // then n is required to instantiate node
                // which means node depends on n
                if (argClass.isAssignableFrom(n.componentInfo.getComponentClass())) {
                    n.dependants.add(node);
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
            throw new ComponentException("cyclical dependency found");
        }

        // set temporary mark to find cycles
        node.mark = Mark.TEMPORARY;

        // visit each dependant
        node.dependants.forEach(this::visit);

        // set permanent mark to avoid visiting this node in the future
        node.mark = Mark.PERMANENT;

        // add node's ComponentInfo to the beginning of the sorted list
        sorted.add(0, node.componentInfo);
    }

    private enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    private static class Node {
        private final ComponentInfo componentInfo;
        private final Set<Node> dependants;
        private Mark mark;

        Node(ComponentInfo componentInfo) {
            this.componentInfo = componentInfo;
            this.dependants = new HashSet<>();
            this.mark = Mark.NONE;
        }
    }
}
