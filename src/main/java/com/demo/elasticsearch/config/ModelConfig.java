package com.demo.elasticsearch.config;


import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ModelConfig {
    public static final String dateFormat ="yyyy-MM-dd";

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<DocumentDto, Document> typeMap = mapper.createTypeMap(DocumentDto.class,Document.class);
        Converter<Date, String> formatDate = ctx -> ctx.getSource() != null
                ? formatDateToString(ctx.getSource())
                : "";
        typeMap.addMappings(m -> m.using(formatDate).map(DocumentDto::getDate, Document::setDate));
        typeMap.addMappings(m -> m.skip(Document::setId));
        return mapper;
    }

    public static String formatDateToString(Date d) {
        return new SimpleDateFormat(dateFormat).format(d).toString();
    }
}
