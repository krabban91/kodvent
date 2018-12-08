package krabban91.kodvent.kodvent.day8;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LicenseTreeNode {
    List<LicenseTreeNode> children = new LinkedList<>();
    List<Integer> metadata = new LinkedList<>();

    public LicenseTreeNode(InputContainer input) {
        List<Integer> header = input.removeNFirst(2);
        Integer childCount = header.get(0);
        Integer metadataCount = header.get(1);
        IntStream.range(0, childCount)
                .forEach(i -> children.add(new LicenseTreeNode(input)));
        metadata = input.removeNFirst(metadataCount)
                .stream()
                .collect(Collectors.toList());
    }

    public int getCheckSum() {
        return Integer.sum(
                metadata.stream()
                        .reduce(0, Integer::sum),
                children.stream()
                        .map(LicenseTreeNode::getCheckSum)
                        .reduce(0, Integer::sum));
    }

    public int getMetaDataSum() {
        return children.size() == 0 ?
                metadata.stream()
                        .reduce(0, Integer::sum) :
                metadata.stream()
                        .map(i -> children.size() < i ? 0 : children.get(i - 1).getMetaDataSum())
                        .reduce(0, Integer::sum);
    }
}
