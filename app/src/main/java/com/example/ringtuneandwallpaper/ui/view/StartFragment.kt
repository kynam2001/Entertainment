package com.example.ringtuneandwallpaper.ui.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ringtuneandwallpaper.databinding.FragmentStartBinding
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class StartFragment: Fragment(){

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ringtuneButton.setOnClickListener{
            lifecycleScope.launch {
                binding.wallpaperButton.text = ""
                binding.wallpaperButton.isClickable = false
                animateBackground(binding.root, getBackgroundColor(it)!!, it)
                val action = StartFragmentDirections.actionStartFragmentToRingTuneFragment()
                findNavController().navigateBack(action)
            }
        }
        binding.wallpaperButton.setOnClickListener {
            lifecycleScope.launch {
                binding.ringtuneButton.text = ""
                binding.ringtuneButton.isClickable = false
                animateBackground(binding.root, getBackgroundColor(it)!!, it)
                val action = StartFragmentDirections.actionStartFragmentToWallpaperFragment()
                findNavController().navigateForward(action)
            }

        }
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()  // Kết thúc Activity khi nhấn nút back
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
    }

    private fun getBackgroundColor(view: View): Int? {
        val background = view.background
        return if (background is ColorDrawable) {
            background.color // Lấy màu từ ColorDrawable
        } else {
            null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun animateBackground(rootLayout: View, color: Int, clickedButton: View) {
        // Xác định vị trí và kích thước ban đầu của button được click
        val startWidth = clickedButton.width
        val endWidth = rootLayout.width

        // Animator để mở rộng background từ button cho đến khi bao phủ toàn bộ màn hình
        return suspendCancellableCoroutine { continuation ->
            val animator = ValueAnimator.ofInt(startWidth, endWidth).apply {
                duration = 600 // Thời gian hiệu ứng
                interpolator = AccelerateDecelerateInterpolator()

                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    // Cập nhật kích thước của background
                    rootLayout.setBackgroundColor(color)
                    clickedButton.layoutParams.width = animatedValue
                    clickedButton.requestLayout()
                }

                addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator) {

                    }

                    override fun onAnimationEnd(p0: Animator) {
                        continuation.resume(Unit, null)
                    }

                    override fun onAnimationCancel(p0: Animator) {
                        continuation.resume(Unit, null)
                    }

                    override fun onAnimationRepeat(p0: Animator) {

                    }

                })
            }

            animator.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}