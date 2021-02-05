package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Flux<Vendor> list(){
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Vendor> getById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher){
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    // vogliamo aggiornare o inserire un nuovo vendor specifico quindi
    // andiamo a definire l'id specifico dell'oggetto in questione
    // se questo è gia presente allora verrà aggiornato altrimenti verrà inserito un nuovo vendor
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
        // vogliamo aggiornare o inserire un nuovo vendor specifico quindi
        // andiamo a definire l'id specifico dell'oggetto in questione
        // se questo è gia presente allora verrà aggiornato altrimenti verrà inserito un nuovo vendor
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();

        if (!foundVendor.getFirstName().equals(vendor.getFirstName())) {
            foundVendor.setFirstName(vendor.getFirstName());

            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
 //      return vendorRepository.findById(id)
 //          .map(foundVendor ->{
 //              int i = 0;
 //              if(!foundVendor.getFirstName().equals(vendor.getFirstName())){
 //                  foundVendor.setFirstName(vendor.getFirstName());
 //                  i++;
 //              }
 //              if(!foundVendor.getLastName().equals(vendor.getLastName())) {
 //                  foundVendor.setLastName(vendor.getLastName());
 //                  i++;
 //              }
 //              if(i>0){
 //                  return vendorRepository.save(foundVendor).block();
 //              }else{
 //                  return Mono.just(foundVendor).block();
 //              }
 //          });
 //  }
}



