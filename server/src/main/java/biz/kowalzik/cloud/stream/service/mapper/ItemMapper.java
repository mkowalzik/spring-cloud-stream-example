package biz.kowalzik.cloud.stream.service.mapper;

import biz.kowalzik.cloud.stream.service.apistream.ItemMessage;
import biz.kowalzik.cloud.stream.service.model.InternalItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ItemMapper {

    ItemMessage mapToApi(InternalItem source);

    InternalItem mapToModel(ItemMessage source);

    List<ItemMessage> mapToApi(List<InternalItem> sources);

    List<InternalItem> mapToModels(List<ItemMessage> sources);
}
