package com.binarycode.InventorySystemBackend.service;

import com.binarycode.InventorySystemBackend.model.Supplier;
import com.binarycode.InventorySystemBackend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findActiveSuppliers();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Optional<Supplier> getSupplierByName(String name) {
        return supplierRepository.findByName(name);
    }


    public Optional<Supplier> getSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setName(supplierDetails.getName()); 
                    supplier.setContactPerson(supplierDetails.getContactPerson());
                    supplier.setPhone(supplierDetails.getPhone());
                    supplier.setEmail(supplierDetails.getEmail());
                    supplier.setAddress(supplierDetails.getAddress());
                    return supplierRepository.save(supplier);
                })
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.findById(id).ifPresent(supplier -> {
            supplier.setActive(false); 
            supplierRepository.save(supplier);
        });
    }

    public List<Supplier> searchSuppliersByName(String name) {
        return supplierRepository.searchActiveSuppliersByName(name);
    }

    public List<Supplier> getSuppliersByPhone(String phone) {
        return supplierRepository.findByPhone(phone);
    }
}