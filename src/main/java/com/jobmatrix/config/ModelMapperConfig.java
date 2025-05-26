package com.jobmatrix.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
////
////        // Add custom converter for UUID to Long conversion
////        Converter<UUID, Long> uuidToLongConverter = new AbstractConverter<UUID, Long>() {
////            @Override
////            protected Long convert(UUID source) {
////                return null; // Return null for UUID to Long conversion
////            }
////        };
////
////        // Add custom converter for Long to UUID conversion
////        Converter<Long, UUID> longToUuidConverter = new AbstractConverter<Long, UUID>() {
////            @Override
////            protected UUID convert(Long source) {
////                return null; // Return null for Long to UUID conversion
////            }
////        };
////
////        modelMapper.addConverter(uuidToLongConverter);
////        modelMapper.addConverter(longToUuidConverter);
//
        return new ModelMapper();

//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.STRICT);
//
//        return modelMapper;
    }
}
