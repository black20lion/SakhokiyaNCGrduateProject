import {Component, Input, OnInit} from '@angular/core';
import {Icon} from "../authorized-page/authorized-page.component";

@Component({
  selector: 'app-icon-authorized',
  templateUrl: './icon-authorized.component.html',
  styleUrls: ['./icon-authorized.component.scss']
})
export class IconAuthorizedComponent implements OnInit {

  @Input() icon!: Icon
  @Input() index!: number

  opacity: number = 0.5

  hrefs: string[] =["./account", './account']

  ngOnInit(): void {
  }

}
