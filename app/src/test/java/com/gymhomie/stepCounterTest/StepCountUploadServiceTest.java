package com.gymhomie.stepCounterTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.service.StepCountUploadService;

public class StepCountUploadServiceTest {

    private StepCountUploadService stepCountUploadService;

    @Before
    public void setUp() {
        // Initialize the service and any dependencies or mocks
        MockitoAnnotations.initMocks(this);
        stepCountUploadService = new StepCountUploadService();

        // Mock FirebaseAuth.getInstance() and auth.getCurrentUser() to return a user with a UID
        FirebaseAuth auth = mock(FirebaseAuth.class);
        when(auth.getCurrentUser()).thenReturn(mock(FirebaseUser.class));
        stepCountUploadService.auth = auth;
    }

    @Test
    public void testDataEntry() {
        // Mock Firestore collection/document interactions
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        when(firestore.collection(anyString())).thenReturn(mock(CollectionReference.class));
        when(firestore.collection(anyString()).document(anyString())).thenReturn(mock(DocumentReference.class));
        stepCountUploadService.firestore = firestore;

        // Call the method you want to test
        stepCountUploadService.dataEntry();

        // Assert that Firestore methods were called as expected
        verify(firestore.collection("users")).document(anyString()).collection("StepCounter").document("Rrm86MEQtYWPskWBZ1yw").set(anyMap());
    }
}

