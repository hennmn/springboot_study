package com.example.cardatabase4.service;

import com.example.cardatabase4.domain.Owner;
import com.example.cardatabase4.domain.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    // 모든 Owner 목록 조회
    public List<Owner> getOwner() {
        return ownerRepository.findAll();
    }

    // 새로운 Owner 저장
    public Owner addOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    // Owner 한 명 조회
    public Optional<Owner> getOwnerById(Long id) {
        return ownerRepository.findById(id);
    }

    // Owner 한 명 삭제
    public boolean deleteOwner(Long id) {
        if(ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Owner 수정
    public Optional<Owner> updateOwner(Long id, Owner ownerDetails) {
        return ownerRepository.findById(id)
                .map(owner -> {
                    owner.setCars(ownerDetails.getCars());
                    owner.setLastName(ownerDetails.getLastName());
                    owner.setFirstName(ownerDetails.getFirstName());
                    return owner;
                });
    }



    /*
    Owner 전체 조회 / id 별 조회 / Owner 객체 추가 /
    Owner 객체 삭제 / Owner 객체 수정
     */
}
