import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatbotService } from '../../services/chatbot';

interface Message {
  text: string;
  isUser: boolean;
  timestamp: Date;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrl: './chat.css'
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;
  
  messages: Message[] = [];
  currentMessage: string = '';
  isLoading: boolean = false;
  error: string | null = null;

  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {
    this.addBotMessage('Bonjour ! Je suis votre assistant IA. Comment puis-je vous aider aujourd\'hui ?');
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  sendMessage(): void {
    if (!this.currentMessage.trim() || this.isLoading) {
      return;
    }

    const userMessage = this.currentMessage.trim();
    this.addUserMessage(userMessage);
    this.currentMessage = '';
    this.isLoading = true;
    this.error = null;

    this.chatbotService.sendMessage(userMessage).subscribe({
      next: (response: string) => {
        this.addBotMessage(response);
        this.isLoading = false;
      },
      error: (err) => {
        this.error = `Erreur: ${err.message || err.statusText || 'Erreur inconnue'}`;
        this.addBotMessage('Désolé, une erreur s\'est produite. Veuillez réessayer.');
        console.error('Erreur chatbot:', err);
        this.isLoading = false;
      }
    });
  }

  addUserMessage(text: string): void {
    this.messages.push({
      text,
      isUser: true,
      timestamp: new Date()
    });
  }

  addBotMessage(text: string): void {
    this.messages.push({
      text,
      isUser: false,
      timestamp: new Date()
    });
  }

  scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop = 
        this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      // Ignore scroll errors
    }
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  formatMessage(text: string): string {
    // Convertit les retours à la ligne en <br>
    return text.replace(/\n/g, '<br>');
  }
}

