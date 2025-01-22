package com.pigeonchat.lab6.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class BaseMapper<RQ, RS, E> implements Mapper<RQ, RS, E> {
    private final ModelMapper modelMapper = new ModelMapper();

    public BaseMapper()
    {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    @Override
    public RS toResponseDTO(E entity, Class<RS> responseClass) {
        return modelMapper.map(entity, responseClass);
    }

    @Override
    public E toEntity(RQ requestDTO, Class<E> entityClass) {
        return modelMapper.map(requestDTO, entityClass);
    }
}
