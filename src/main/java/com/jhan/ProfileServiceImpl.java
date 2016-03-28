package com.jhan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by jhan on 3/27/16.
 */
@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    @Transactional
    public Profile findById(String id) {
        return profileRepository.findOne(id);
    }

    @Override
    @Transactional
    public Profile update(Profile profile) {
        Profile updatedProfile = profileRepository.findOne(profile.getId());

        updatedProfile.setFirstname(profile.getFirstname());
        updatedProfile.setLastname(profile.getLastname());
        updatedProfile.setEmail(profile.getEmail());
        updatedProfile.setAddress(profile.getAddress());
        updatedProfile.setOrganization(profile.getOrganization());
        updatedProfile.setAboutMyself(profile.getAboutMyself());
        return updatedProfile;
    }

    @Override
    @Transactional
    public Profile create(Profile profile) {
        Profile createProfile = profile;
        return profileRepository.save(createProfile);
    }

    @Override
    @Transactional
    public Profile delete(String id) {
        Profile deleteProfile = profileRepository.findOne(id);
        profileRepository.delete(deleteProfile);
        return deleteProfile;
    }
}
