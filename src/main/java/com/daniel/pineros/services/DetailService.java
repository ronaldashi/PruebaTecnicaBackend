package com.daniel.pineros.services;

import com.daniel.pineros.entities.Detail;
import com.daniel.pineros.repositories.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetailService {

    private final DetailRepository detailRepository;

    @Autowired
    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }
    public void createDetail(Detail detail){
        this.detailRepository.save(detail);
    }
    public List<Detail> getDetailBySale(String saleId){
        return this.detailRepository.findBySale_Id(saleId);
    }
}
