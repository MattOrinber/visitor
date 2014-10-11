package org.visitor.appportal.repository;

import org.visitor.appportal.domain.Label;
import org.visitor.appportal.repository.base.BaseRepository;

import java.util.List;

public interface LabelRepository extends BaseRepository<Label, Integer> {
    Label findByLabelId(Integer labelId);//labelId

    Label findByName(String name);
}
