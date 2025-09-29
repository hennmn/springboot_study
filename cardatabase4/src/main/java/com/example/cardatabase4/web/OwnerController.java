package com.example.cardatabase4.web;

import com.example.cardatabase4.domain.Car;
import com.example.cardatabase4.domain.Owner;
import com.example.cardatabase4.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    // 1. 모든 Owner 정보 조회 (GET /api/owners)
    @GetMapping("/owners")
    public List<Owner> getOwners() {
        return ownerService.getOwner();
    }

    // 2. Owner 한 명 추가(POST /api/cars)
    @PostMapping("/owers")
    public ResponseEntity<Owner> addOwner(@RequestBody Owner owner) {
        Owner savedOwner = ownerService.addOwner(owner);

        return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
    }

    // 3. Owner 한 명 조회
    @GetMapping("owners/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        return ownerService.getOwnerById(id)
                .map(owner -> ResponseEntity.ok().body(owner))
                .orElse(ResponseEntity.notFound().build());
    }

    // 차량 한 대 삭제
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        if(ownerService.deleteOwner(id)) {
            return ResponseEntity.noContent().build();  // 삭제 성공시 204 No Content return
        } else {
            return ResponseEntity.notFound().build();   // 대상이 없으면 404 Not Found
        }
    }

    // 5. 차량 한 대 수정
    @PutMapping("/owners/{id}")
    public ResponseEntity<Owner> updateCar(@PathVariable Long id, @RequestBody Owner ownerDetails) {
        return ownerService.updateOwner(id, ownerDetails)
                .map(updateOwner -> ResponseEntity.ok().body(updateOwner)) // 수정 성공 시에는 200 OK
                .orElse(ResponseEntity.notFound().build()); // 수정할 차량 id 검색 실패시 404 Not Found
    }




}
