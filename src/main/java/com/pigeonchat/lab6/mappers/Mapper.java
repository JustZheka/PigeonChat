package com.pigeonchat.lab6.mappers;

public interface Mapper<RQ, RS, E> {
    RS toResponseDTO(E entity, Class<RS> responseClass);

    E toEntity(RQ requestDTO, Class<E> entityClass);
}
