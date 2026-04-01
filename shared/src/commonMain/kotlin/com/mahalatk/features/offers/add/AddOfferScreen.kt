package com.mahalatk.features.offers.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.ButtonStyle
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.stepper.StepIndicator
import com.mahalatk.features.offers.add.steps.Step1OfferTypeSelection
import com.mahalatk.features.offers.add.steps.Step2LogicSetup
import com.mahalatk.features.offers.add.steps.Step3Scope
import com.mahalatk.features.offers.add.steps.Step4DurationReview
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.add_offer
import mahalatk.shared.generated.resources.next_step
import mahalatk.shared.generated.resources.offer_published_success
import mahalatk.shared.generated.resources.previous_step
import mahalatk.shared.generated.resources.publish_offer
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val LAST_STEP = 3

@Composable
fun AddOfferScreen(
    viewModel: AddOfferViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.add_offer),
            onBackClick = onBack,
        )

        StepIndicator(
            totalSteps = 4,
            currentStep = state.currentStep,
        )

        StepError(errorRes = state.stepError)

        StepContent(
            currentStep = state.currentStep,
            state = state,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )

        BottomNavigation(
            currentStep = state.currentStep,
            onPrevious = viewModel::previousStep,
            onNext = if (state.currentStep == LAST_STEP) viewModel::publish else viewModel::nextStep,
        )
    }

    SuccessBottomSheet(
        message = stringResource(Res.string.offer_published_success),
        visible = state.showSuccess,
        onDismiss = onBack,
    )
}

@Composable
private fun StepError(errorRes: org.jetbrains.compose.resources.StringResource?) {
    if (errorRes == null) return
    Text(
        text = stringResource(errorRes),
        style = MahalatkTheme.bodySmall,
        color = AppColor.Error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun StepContent(
    currentStep: Int,
    state: AddOfferState,
    viewModel: AddOfferViewModel,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = currentStep,
        modifier = modifier,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            } else {
                (slideInHorizontally { -it } + fadeIn()) togetherWith
                        (slideOutHorizontally { it } + fadeOut())
            }
        },
        label = "step_content",
    ) { step ->
        when (step) {
            0 -> Step1OfferTypeSelection(state = state, viewModel = viewModel)
            1 -> Step2LogicSetup(state = state, viewModel = viewModel)
            2 -> Step3Scope(state = state, viewModel = viewModel)
            3 -> Step4DurationReview(state = state, viewModel = viewModel)
        }
    }
}

@Composable
private fun BottomNavigation(
    currentStep: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (currentStep > 0) {
            DefaultButton(
                text = stringResource(Res.string.previous_step),
                style = ButtonStyle.OUTLINED,
                modifier = Modifier.weight(1f),
                onClick = onPrevious,
            )
        }
        DefaultButton(
            text = if (currentStep == LAST_STEP) {
                stringResource(Res.string.publish_offer)
            } else {
                stringResource(Res.string.next_step)
            },
            modifier = Modifier.weight(1f),
            onClick = onNext,
        )
    }
}
